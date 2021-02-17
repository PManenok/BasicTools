package by.esas.tools.accesscontainer

import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import by.esas.tools.accesscontainer.dialog.BiometricUserInfo
import by.esas.tools.accesscontainer.dialog.DialogProvider
import by.esas.tools.accesscontainer.dialog.IBaseDialog
import by.esas.tools.accesscontainer.dialog.IBiometric
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.RefreshResult
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.accesscontainer.error.ErrorStatusEnum
import by.esas.tools.accesscontainer.error.IErrorMapper
import by.esas.tools.accesscontainer.support.IContainerCancellationCallback
import by.esas.tools.accesscontainer.support.IContainerExecutor
import by.esas.tools.accesscontainer.support.ITypeManager
import by.esas.tools.accesscontainer.support.supporter.Supporter
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.ILogger
import java.lang.ref.WeakReference
import javax.crypto.Cipher
import javax.crypto.SecretKey

class Refresher<E : Enum<E>, M : BaseErrorModel<E>>(
    private val logger: ILogger<E, M>,
    private val executor: IContainerExecutor<E, M>,
    private val typeManager: ITypeManager,
    private val userInfo: BiometricUserInfo,
    private val mapper: IErrorMapper<E, M>,
    private val dialogProvider: DialogProvider,
    private val supporter: Supporter<E, M>
) : IRefreshContainer<E, M> {
    val TAG: String = Refresher::class.java.simpleName
    private var accessToken: String = ""
    private var refreshToken: String = ""

    // default cancellation callback
    private val defaultCancellationCallback: IContainerCancellationCallback = object :
        IContainerCancellationCallback {
        override fun onCancel() {
            logger.log("defaultCancellationCallback onCancel does nothing")
            //does nothing. should be set before refresh usage
        }
    }
    private var cancellationCallback: IContainerCancellationCallback =
        defaultCancellationCallback // cancellation callback invoke if UC callback not set?

    private var fragmentManager: WeakReference<FragmentManager?> = WeakReference(null) // fragment manager for simple dialogs
    private var activity: WeakReference<FragmentActivity?> = WeakReference(null) // activity for biometric dialog

    private var result: ContainerRequest<RefreshResult, E, M> =
        ContainerRequest<RefreshResult, E, M>() // result instance which will get result when it is finished
    private var secretResult: ContainerRequest<String, E, M> =
        ContainerRequest<String, E, M>() // result instance which will get result when it is finished
    private var userId: String = "" // user ID sets from getSecrets or with setUserId method

    private var isBiometricAvailable: Boolean = false // shows if biometric is available
    private var needCheck: Boolean = false // enable check scenario
    private var needSecret: Boolean = false // enable secret scenario
    private var refreshExplicitly: Boolean = false // enable refresh in check access

    private var presentType: AuthType = AuthType.NONE // secret type that user uses at the moment

    private var stateIsPaused: Boolean = false // app is in pause
    private var lastAction: () -> Unit = {} // last action which will be invoked after resume

    private var currentDialog: IBaseDialog? = null // current(last) simple dialog
    private var currentBiometricDialog: IBiometric? = null // current(last) biometric dialog

    private var forgotPasswordActionEnable: Boolean = false // forgot password Flag
    private var forgotPasswordAction: () -> Unit = {} // action for forgot password

    private val types: MutableSet<AuthType> = mutableSetOf()// shows which secret types user has

    private var refresherErrorStatusToCheck: E? = null
    private var checkRefreshError: Boolean = false
    private var useExtraCheck: Boolean = false
    private var extraRefreshCheck: (BaseErrorModel<E>) -> Unit = {}

    init {
        logger.setTag(TAG)
    }

    override fun setUserId(userId: String) {
        this.userId = userId
    }

    override fun getToken(): String {
        logger.log("getToken()")
        return accessToken
    }

    override fun setToken(token: Token) {
        logger.log("setToken() refreshToken isNullOrBlank = ${token.refreshToken.isNullOrBlank()}")
        accessToken = token.accessToken
        refreshToken = token.refreshToken ?: ""
    }

    /**
     * Запуск запроса на восстановление маркера доступа с последующим обновлением
     * данных контейнера и выполнением переданного блока кода
     *
     */
    override fun refresh(onComplete: (String?) -> Unit, onError: (BaseErrorModel<E>) -> Unit, onCancel: () -> Unit) {
        logger.log("refresh")
        this.refreshExplicitly = true
        result = ContainerRequest<RefreshResult, E, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.log("refresh result.onComplete token != null = ${tokenResponse.token != null}")
                    tokenResponse.token?.let { setToken(it) }
                    onComplete(tokenResponse.token?.accessToken)
                }
                onError {
                    logger.log("refresh result.onError")
                    logger.logError(it)
                    onError(it)
                }
                onCancel {
                    logger.log("refresh result.onCancel")
                    onCancel()
                }
            }

        logger.log("refresh needCheck = false")
        needCheck = false
        needSecret = false
        if (refreshToken.isNotEmpty()) {
            logger.log("refresh refreshToken isNotEmpty")
            refreshAccess(refreshToken)
        } else {
            logger.log("refresh refreshToken isEmpty")
            userId = userInfo.getCurrentUser()
            logger.log("refresh userId = $userId")
            getSecrets()
        }
    }

    override fun onCancel() {
        logger.log("onCancel")
        cancellationCallback.onCancel()
    }

    override fun checkAccess(refreshExplicitly: Boolean, response: ContainerRequest<String, E, M>.() -> Unit) {
        logger.log("checkAccess refreshExplicitly = $refreshExplicitly")
        this.refreshExplicitly = refreshExplicitly
        val result = ContainerRequest<String, E, M>().apply(response)

        this.result = ContainerRequest<RefreshResult, E, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.log("checkAccess result.onComplete hasAccess = ${tokenResponse.hasAccess}")
                    if (tokenResponse.hasAccess) {
                        logger.log("checkAccess result.onComplete token != null = ${tokenResponse.token != null}")
                        tokenResponse.token?.let { token ->
                            setToken(token)
                        }
                        result(accessToken)
                    } else result()
                }
                onError {
                    logger.log("checkAccess result.onError")
                    logger.logError(it)
                    result(it)
                }
                onCancel {
                    logger.log("checkAccess result.onCancel")
                    result()
                }
            }
        logger.log("checkAccess needCheck = true")
        needCheck = true
        needSecret = false
        userId = userInfo.getCurrentUser()
        logger.log("checkAccess userId = $userId")
        getSecrets()
    }

    override fun getSecret(response: ContainerRequest<String, E, M>.() -> Unit) {
        logger.log("getSecret")
        val result = ContainerRequest<String, E, M>().apply(response)

        this.secretResult = result
        logger.log("getSecret needSecret = true, needCheck = false")
        needSecret = true
        needCheck = false
        userId = userInfo.getCurrentUser()
        logger.log("getSecret userId = $userId")
        getSecrets()
    }

    override fun saveRefresh(refreshToken: String, response: ContainerRequest<String, E, M>.() -> Unit) {
        val result = ContainerRequest<String, E, M>().apply(response)

        this.result = ContainerRequest<RefreshResult, E, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.log("saveRefresh result.onComplete")
                    result(accessToken)
                }
                onError {
                    logger.log("saveRefresh result.onError")
                    logger.logError(it)
                    result(it)
                }
                onCancel {
                    logger.log("saveRefresh result.onCancel")
                    result()
                }
            }

        if (isBiometricAvailable) {
            showEncryptBiometricDialog(Token(accessToken, refreshToken))
        } else {
            showEncryptPinDialog(Token(accessToken, refreshToken))
        }
    }

    /*############################ Simple refresh token #########################################*/

    private fun refreshAccess(refreshToken: String) {
        logger.log("refreshAccess")
        executor.refreshAccessUC(refreshToken) {
            onComplete { token ->
                logger.log("refreshAccess onComplete it != null ${token != null}")
                if (token != null) {
                    result(RefreshResult(token = token))
                }
            }
            onError { error ->
                logger.log("refreshAccess onError")
                if (checkRefreshError) {
                    checkRefreshError(error)
                } else {
                    result(error)
                }
            }
        }
    }

    private fun checkRefreshError(error: M) {
        if (useExtraCheck) {
            extraRefreshCheck(error)
        } else {
            if (error.statusEnum == refresherErrorStatusToCheck) {
                //deleteSecrets()
                showPasswordDialog(true)
            } else
                result(error)
        }
    }

    /*############################# Simple refresh token END ####################################*/

    /*############################# Refresh token with dialogs ##################################*/

    private fun getSecrets() {
        logger.log("getSecrets")
        types.clear()
        executor.getSecretsUC {
            onComplete {
                logger.log("getSecrets onComplete types not empty = ${it.isNotEmpty()}")
                if (it.isNotEmpty()) {
                    types.addAll(it)
                    logger.log("getSecrets onComplete onlyOnePresent = ${it.size == 1}")
                    if (it.size == 1) {
                        when (it[0]) {
                            AuthType.PIN_AUTH -> {
                                logger.log("getSecrets onComplete presentType = PIN_AUTH")
                                presentType = AuthType.PIN_AUTH
                                showDecryptPinDialog()
                            }
                            AuthType.BIOMETRIC_AUTH -> {
                                logger.log("getSecrets onComplete presentType = BIOMETRIC_AUTH")
                                presentType = AuthType.BIOMETRIC_AUTH
                                showDecryptBiometricDialog()
                            }
                            AuthType.NONE -> {
                                if (needSecret) secretResult(supporter.util.createErrorModel(0, ErrorStatusEnum.HAS_NO_SECRETS.name))
                                else showPasswordDialog()
                            }
                        }
                    } else {
                        when (typeManager.getPreferredType()) {
                            AuthType.PIN_AUTH -> showDecryptPinDialog()
                            AuthType.BIOMETRIC_AUTH -> showDecryptBiometricDialog()
                            AuthType.NONE -> showAuthDialog(isDecrypting = true)
                        }
                    }
                } else {
                    if (needSecret) secretResult(supporter.util.createErrorModel(0, ErrorStatusEnum.HAS_NO_SECRETS.name))
                    else showPasswordDialog()
                }
            }
            onError { throwable ->
                logger.log("getSecrets onError")
                result(throwable)
            }
        }
    }

    /*private fun refreshWithSecret(pinKey: SecretKey? = null, cipher: Cipher? = null) {
        logger.log("refreshWithSecret pinKey!=null ${pinKey != null}, cipher!=null ${cipher != null}")
        executor.refreshWithSecretUC(pinKey, cipher) {
            onComplete { token ->
                logger.log("refreshWithSecret onComplete")
                result(RefreshResult(token))
            }

            onError { error ->
                logger.log("refreshWithSecret onError")
                if (checkRefreshError) {
                    checkRefreshError(error)
                } else {
                    result(error)
                }
            }
        }
    }*/
    /*############################# Refresh token with dialogs END ##############################*/

    /*############################# Check access with dialogs ###################################*/

    private fun checkSecret(pinKey: SecretKey? = null, cipher: Cipher? = null) {
        logger.log("checkSecret pinKey!=null ${pinKey != null}, cipher!=null ${cipher != null}")
        executor.checkSecretUC(pinKey, cipher) {
            onComplete {
                logger.log("checkSecret onComplete ${it.first}")
                if (it.first) {
                    when {
                        needSecret -> secretResult(it.second)
                        refreshExplicitly -> {
                            logger.log("checkSecret onComplete refreshToken != null")
                            refreshWithSecret(it.second)
                        }
                        else -> {
                            logger.log("checkSecret onComplete return")
                            result(RefreshResult())
                        }
                    }
                } else {
                    logger.log("checkSecret onComplete failed check return error SECRET_CHECK_NOT_MATCH")
                    val error = supporter.util.createErrorModel(0, ErrorStatusEnum.SECRET_CHECK_NOT_MATCH.name)
                    if (needSecret) secretResult(error)
                    else result(error)
                }
            }
            onError { error ->
                logger.log("checkSecret onError")
                result(error)
            }
        }
    }

    private fun refreshWithSecret(secret: String) {
        logger.log("refreshWithSecret")
        executor.refreshWithSecretUC(secret) {
            onComplete { token ->
                logger.log("refreshWithSecret onComplete")
                result(RefreshResult(token))
            }

            onError { error ->
                logger.log("refreshWithSecret onError")
                if (checkRefreshError) {
                    checkRefreshError(error)
                } else {
                    result(error)
                }
            }
        }
    }

    /*############################## Check access with dialogs END ##############################*/

    /*############################### Authentication with password ##############################*/
    private fun authenticate(password: String, recreate: Boolean, noSecrets: Boolean) {
        logger.log("authenticate password not blank = ${password.isNotBlank()}")
        if (password.isNotBlank()) {
            executor.authorizationInSystemUC(password) {
                onComplete { token ->
                    logger.log("authenticate onComplete it.refreshToken != null ${token.refreshToken != null}")
                    if (token.refreshToken != null) {
                        logger.log("authenticate onComplete isBiometricAvailable $isBiometricAvailable")

                        if (recreate || types.isEmpty()) {
                            if (isBiometricAvailable) {
                                showEncryptBiometricDialog(token)
                            } else {
                                showEncryptPinDialog(token)
                            }
                        } else {
                            result(RefreshResult(token))
                        }
                    } else {
                        logger.log("authenticate onComplete return")
                        result(RefreshResult(token))
                    }
                }
                onError { error ->
                    logger.log("authenticate onError")
                    result(error)
                }
            }
        } else {
            logger.log("authenticate showPasswordDialog")
            showPasswordDialog(noSecrets)
        }
    }

    private fun putSecrets(token: Token, cipher: Cipher? = null, pinKey: SecretKey? = null) {
        logger.log("putSecrets pinKey!=null ${pinKey != null}, cipher!=null ${cipher != null}")
        executor.createSecretUC(token.refreshToken!!, cipher, pinKey) {
            onComplete {
                logger.log("putSecrets onComplete return")
                result(RefreshResult(token))
            }
            onError {
                logger.log("putSecrets onError return")
                logger.showMessage("Secret was not saved") //todo show message secret was not create
                result(RefreshResult(token))
            }
        }
    }
    /*############################## Authentication with password END ###########################*/

    /*############################### Dialogs ###################################################*/

    private fun showAuthDialog(isDecrypting: Boolean, token: Token? = null) {
        logger.log("showAuthDialog isDecrypting ${isDecrypting}, token!=null = ${token != null}")
        dialogProvider.menuDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e ->
                logger.log("showAuthDialog onError ${e.message}")
                if (needSecret) secretResult(mapper(e))
                else result(mapper(e))
            }, { afterOk ->
                logger.log("showAuthDialog onDismiss afterOk=${afterOk}")
                if (!afterOk) {
                    if (needSecret) secretResult()
                    else result()
                }
            })
            dialog.isDecrypting(isDecrypting)
            dialog.showPassword(!needSecret)
            dialog.setCallBacks({
                logger.log("showAuthDialog onPinClick")
                if (isDecrypting) showDecryptPinDialog()
                else token?.let { showEncryptPinDialog(token) }
            }, {
                logger.log("showAuthDialog onBiometricClick")
                if (isDecrypting) showDecryptBiometricDialog()
                else token?.let { showEncryptBiometricDialog(token) }
            }, {
                logger.log("showAuthDialog onPasswordClick")
                showPasswordDialog()
            })

            val titleRes: Int = if (isDecrypting) -1 else supporter.resProvider.provideEncryptTitle()
            dialog.setTitle(titleRes)
            if (isDecrypting) {
                dialog.setPinPresent(types.contains(AuthType.PIN_AUTH))
                dialog.setBiometricPresent(types.contains(AuthType.BIOMETRIC_AUTH))
            } else {
                dialog.setPinPresent(true)
                dialog.setBiometricPresent(isBiometricAvailable)
            }
            showDialog(dialog.getDialog(), "OperatorAuthDialog")
        }
    }

    /*######################## PIN ##########################*/
    private fun showDecryptPinDialog() {
        logger.log("showDecryptPinDialog")
        dialogProvider.pinDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e ->
                logger.log("showDecryptPinDialog onError ${e.message}")
                if (needSecret) secretResult(mapper(e))
                else result(mapper(e))
            }, { afterOk ->
                logger.log("showDecryptPinDialog onDismiss afterOk=$afterOk")
                if (!afterOk) {
                    if (needSecret) secretResult()
                    else result()
                }
            })

            dialog.setCallbacks({ pin ->
                logger.log("showDecryptPinDialog onPinComplete needCheck=$needCheck")
                val pinKey = supporter.util.generatePin(
                    pin, userId,
                    Settings.Secure.getString(activity.get()?.contentResolver, Settings.Secure.ANDROID_ID)
                )
                typeManager.putPreferredType(AuthType.PIN_AUTH)
                //if (needCheck) {
                checkSecret(pinKey = pinKey)
                /*} else {
                    refreshWithSecret(pinKey = pinKey)
                }*/
            }, {
                logger.log("showDecryptPinDialog onPinCanceled showAuthDialog(isDecrypting = true)")
                showAuthDialog(isDecrypting = true)
            })

            logger.log("showDecryptPinDialog needCheck=$needCheck")
            if (needCheck)
                dialog.setTitle(supporter.resProvider.provideAccessConfirmStr())
            else
                dialog.setTitle(supporter.resProvider.provideAccessStr())
            val cancelId = supporter.resProvider.provideAlterCancelStr()
            dialog.setCancelTitle(cancelId)
            dialog.setCancellable(false)
            if (needSecret) dialog.setHasAnother(types.size > 1)
            else dialog.setHasAnother(true)
            showDialog(dialog.getDialog(), "OperatorPinDecryptDialog")
        }
    }

    private fun showEncryptPinDialog(token: Token) {
        logger.log("showEncryptPinDialog")
        dialogProvider.pinDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e ->
                logger.log("showDecryptPinDialog onError ${e.message}")
                result(mapper(e))
            }, { afterOk ->
                logger.log("showEncryptPinDialog onDismiss afterOk=$afterOk")
                if (!afterOk) {
                    result(
                        RefreshResult(
                            token
                        )
                    )
                }
            })
            dialog.setCallbacks({ pin ->
                logger.log("showEncryptPinDialog onPinComplete preferredType=PIN_AUTH")
                val pinKey = supporter.util.generatePin(
                    pin, userId,
                    Settings.Secure.getString(activity.get()?.contentResolver, Settings.Secure.ANDROID_ID)
                )
                typeManager.putPreferredType(AuthType.PIN_AUTH)
                putSecrets(token = token, pinKey = pinKey)
            }, {
                logger.log("showEncryptPinDialog onPinCanceled showAuthDialog(isDecrypting = false)")
                showAuthDialog(isDecrypting = false, token = token)
            })

            dialog.setTitle(supporter.resProvider.provideEncryptTitle())
            logger.log("showEncryptPinDialog isBiometricAvailable=$isBiometricAvailable")
            val cancelId = if (isBiometricAvailable) supporter.resProvider.provideAlterCancelStr() else -1
            dialog.setCancelTitle(cancelId)
            dialog.setCancellable(false)
            showDialog(dialog.getDialog(), "OperatorPinEncryptDialog")
        }
    }

    /*######################## PIN  END #####################*/

    /*###################### Biometric ######################*/

    private fun showDecryptBiometricDialog() {
        logger.log("showDecryptBiometricDialog")
        dialogProvider.biom.let { dialog ->
            activity.get()?.let { context ->
                logger.log("showDecryptBiometricDialog activity let")
                dialog.onAuthenticationCallback({ isCancelled, errString ->
                    context.runOnUiThread {
                        if (isCancelled) {//Операция с отпечатком отменена. on pause
                            logger.log("$errString isPaused = $stateIsPaused")
                            if (!stateIsPaused) { // somehow dialog set error_canceled after resume if it was unblocked not with finger
                                logger.log("Show decrypt biometric dialog again")
                                showDecryptBiometricDialog()
                            } else {
                                logger.log("Set last action to show decrypt biometric dialog")
                                lastAction = { showDecryptBiometricDialog() }
                            }
                        } else {
                            logger.showMessage(errString)
                            if (needSecret) {
                                if (types.size > 1) showAuthDialog(isDecrypting = true)
                                else secretResult()
                            } else
                                showAuthDialog(isDecrypting = true)
                        }
                    }
                }, { cipher ->
                    logger.log("DecryptBiometricDialog preferredType BIOMETRIC_AUTH")
                    typeManager.putPreferredType(AuthType.BIOMETRIC_AUTH)
                    logger.log("DecryptBiometricDialog needCheck $needCheck")
                    //if (needCheck) {
                    checkSecret(cipher = cipher)
                    /*} else {
                        refreshWithSecret(cipher = cipher)
                    }*/
                })

                logger.log("showDecryptBiometricDialog needCheck=$needCheck")

                dialog.create(context, userInfo)
                val title: String = supporter.resProvider.getString(
                    if (needCheck) supporter.resProvider.provideAccessConfirmStr()
                    else supporter.resProvider.provideAccessStr()
                )
                val negativeText: String = if (needSecret) {
                    val negativeId = if (types.size > 1) supporter.resProvider.provideAlterCancelStr()
                    else supporter.resProvider.provideCancelStr()
                    supporter.resProvider.getString(negativeId)
                } else {
                    val negativeId = supporter.resProvider.provideAlterCancelStr()
                    supporter.resProvider.getString(negativeId)
                }
                dialog.setInfo(title = title, negativeText = negativeText)

                lastAction = {
                    logger.log("showDecryptBiometricDialog lastAction DECRYPT_MODE")
                    try {
                        currentBiometricDialog = dialog.authenticate(isDecrypt = true)
                    } catch (e: Exception) {
                        logger.log("showDecryptBiometricDialog catch exception")
                        logger.logError(e)
                        deleteBiometricType()
                    }
                }
                logger.log("showDecryptBiometricDialog stateIsPaused=$stateIsPaused")
                if (!stateIsPaused) {
                    lastAction.invoke()
                    lastAction = {}
                }
            }
        }
    }

    private fun showEncryptBiometricDialog(token: Token) {
        logger.log("showEncryptBiometricDialog")

        dialogProvider.biom.let { dialog ->
            activity.get()?.let { context ->
                logger.log("showDecryptBiometricDialog activity let")
                dialog.onAuthenticationCallback({ isCancelled, errString ->
                    context.runOnUiThread {
                        if (isCancelled) {//Операция с отпечатком отменена. on pause
                            logger.log("showEncryptBiometricDialog ERROR_CANCELED")
                            lastAction = { showEncryptBiometricDialog(token) }
                        } else {
                            logger.log("showEncryptBiometricDialog showAuthDialog(isDecrypting = false,token)")
                            showAuthDialog(isDecrypting = false, token = token)
                        }
                    }
                }, { cipher ->
                    logger.log("EncryptBiometricDialog Success preferredType BIOMETRIC_AUTH")
                    typeManager.putPreferredType(AuthType.BIOMETRIC_AUTH)
                    putSecrets(token = token, cipher = cipher)
                })


                dialog.create(context, userInfo)
                val title: String = supporter.resProvider.getString(supporter.resProvider.provideEncryptTitle())
                val negativeText = supporter.resProvider.getString(supporter.resProvider.provideAlterCancelStr())
                dialog.setInfo(title, negativeText)
                logger.log("showEncryptBiometricDialog stateIsPaused=$stateIsPaused")
                lastAction = {
                    logger.log("showEncryptBiometricDialog lastAction ENCRYPT_MODE")
                    try {
                        currentBiometricDialog = dialog.authenticate(isDecrypt = false)
                    } catch (e: Exception) {
                        logger.log("showEncryptBiometricDialog catch exception")
                        logger.logError(e)
                        isBiometricAvailable = false
                        showEncryptPinDialog(token)
                    }
                }
                if (!stateIsPaused) {
                    lastAction.invoke()
                    lastAction = {}
                }
            }
        }
    }

/*#################### Biometric END ####################*/


/*##################### Password ########################*/

    private fun showPasswordDialog(noSecrets: Boolean = false) {
        logger.log("showPasswordDialog")
        dialogProvider.passwordDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e: Exception ->
                logger.log("showPasswordDialog onError ${e.message}")
                result(mapper(e))
            }, { afterOk: Boolean ->
                logger.log("showPasswordDialog onDismiss afterOk=$afterOk, types not empty=${types.isNotEmpty()}")
                if (!afterOk) {
                    if (!noSecrets && types.isNotEmpty()) {
                        showAuthDialog(isDecrypting = true)
                    } else {
                        result()
                    }
                }
                /*if (!afterOk && types.isNotEmpty()) {
                    showAuthDialog(isDecrypting = true)
                } else if (!afterOk && types.isEmpty()) {
                    result()
                }*/
            })
            logger.log("showPasswordDialog types not empty=${types.isNotEmpty()}")
            if (!noSecrets && types.isNotEmpty()) {
                dialog.setCancelTitle(supporter.resProvider.provideAlterCancelStr())
                dialog.setShowRecreateAuth(true)
            }
            logger.log("showPasswordDialog forgotPasswordActionEnable=$forgotPasswordActionEnable")
            dialog.setForgotPassword(forgotPasswordActionEnable)
            logger.log("showPasswordDialog needCheck=$needCheck")
            if (needCheck)
                dialog.setTitle(supporter.resProvider.provideAccessConfirmStr())
            else
                dialog.setTitle(supporter.resProvider.provideAccessStr())
            dialog.setCallbacks({ password: String, recreate: Boolean ->
                logger.log("showPasswordDialog onPasswordComplete")
                authenticate(password, recreate, noSecrets)
            }, {
                logger.log("showPasswordDialog onPasswordForgot forgotPasswordActionEnable=$forgotPasswordActionEnable")
                if (forgotPasswordActionEnable)
                    forgotPasswordAction()
            })

            showDialog(dialog.getDialog(), "PasswordDialog")
        }
    }


/*##################### Password END ####################*/

/*################################## Dialogs END ############################################*/

/*################################## Common #################################################*/

    private fun showDialog(dialog: IBaseDialog, tag: String) {
        logger.log("showDialog $tag stateIsPaused=$stateIsPaused")
        lastAction = {
            logger.log("showDialog lastAction invoke")
            val manager = fragmentManager.get()
            val context = activity.get()
            logger.log("showDialog lastAction fragmentManager != null ${manager != null} context != null ${context != null}")
            currentDialog = dialog
            dialog.show(manager, context, tag)
        }
        if (!stateIsPaused) {
            lastAction.invoke()
            lastAction = {}
        }
    }

    private fun deleteBiometricType() {
        logger.log("deleteBiometricType BIOMETRIC_AUTH")
        executor.deleteSecretTypeUC(AuthType.BIOMETRIC_AUTH) {
            onComplete {
                logger.log("deleteBiometricType $it")
                if (it) {
                    types.remove(AuthType.BIOMETRIC_AUTH)
                    if (types.isEmpty()) {
                        presentType = AuthType.NONE
                        logger.log("deleteBiometricType containsSecrets = false presentType=NONE")
                        showPasswordDialog()
                    } else {
                        presentType = types.firstOrNull() ?: AuthType.NONE
                        typeManager.putPreferredType(presentType)
                        logger.log("deleteBiometricType presentType=$presentType")
                        showAuthDialog(isDecrypting = true)
                    }
                }
            }
            onError {
                logger.logError(it)
                logger.log("deleteBiometricType showAuthDialog(isDecrypting = true)")
                showAuthDialog(isDecrypting = true)
            }
        }
    }

    /*fun deleteSecrets() {
        logger.log("deleteBiometricType BIOMETRIC_AUTH")
        executor.deleteSecretsUC {
            onComplete {
                logger.log("deleteBiometricType $it")
                if (it) {
                    types.clear()
                    if (types.isEmpty()) {
                        presentType = AuthType.NONE
                        logger.log("deleteBiometricType containsSecrets = false presentType=NONE")
                        showPasswordDialog()
                    } else {
                        presentType = types.firstOrNull() ?: AuthType.NONE
                        typeManager.putPreferredType(presentType)
                        logger.log("deleteBiometricType presentType=$presentType")
                        showAuthDialog(isDecrypting = true)
                    }
                }
            }
            onError {
                logger.logError(it)
                logger.log("deleteBiometricType showAuthDialog(isDecrypting = true)")
                showAuthDialog(isDecrypting = true)
            }
        }
    }*/
/*#################################### Common END ###########################################*/

/*############################## Settings ###################################################*/
    /**
     * Установка Activity необходимая для работы диалогов
     *
     * @param activity FragmentActivity
     */
    override fun setActivity(activity: FragmentActivity) {
        logger.log("setActivity")
        this.activity = WeakReference(activity)
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun doAfterResume() {
                logger.log("activity LifecycleObserver doAfterResume")
                stateIsPaused = false
                lastAction.invoke()
                lastAction = {}
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun doAfterPause() {
                logger.log("activity LifecycleObserver doAfterPause")
                stateIsPaused = true
            }
        })
        isBiometricAvailable = supporter.util.checkBiometricSupport(activity.applicationContext) { tag: String, msg: String ->
            logger.log(tag, msg)
        }
        logger.log("setActivity isBiometricAvailable=$isBiometricAvailable")
    }

    override fun setForgotPasswordAction(enable: Boolean, forgotPasswordAction: () -> Unit) {
        logger.log("setForgotPasswordAction enable=$enable")
        this.forgotPasswordActionEnable = enable
        this.forgotPasswordAction = if (enable) forgotPasswordAction else ({})
    }

    /**
     * Установка Params необходимая для работы диалогов
     *
     * @param root ViewGroup
     * @param manager FragmentManager
     */
    override fun setParams(manager: FragmentManager) {
        logger.log("setParams")
        fragmentManager = WeakReference(manager)
    }

    override fun setCancellationCallback(callback: IContainerCancellationCallback) {
        logger.log("setCancellationCallback")
        cancellationCallback = callback
    }

/*############################## Settings END ###############################################*/

    override fun clearAccess() {
        logger.log("clearAccess")
        accessToken = ""
        refreshToken = ""
        executor.unsubscribe()
    }

    override fun clear() {
        logger.log("clear")
        clearAccess()
        activity.clear()
        fragmentManager.clear()
        cancellationCallback = defaultCancellationCallback
        result = ContainerRequest()
        userId = ""
        isBiometricAvailable = false
        needCheck = false
        refreshExplicitly = false
        types.clear()
        presentType = AuthType.NONE
        stateIsPaused = false
        lastAction = {}
        currentDialog?.dismiss()
        currentDialog = null
        currentBiometricDialog?.cancelAuthentication()
        currentBiometricDialog = null
        dialogProvider.menuDialog.clear()
        dialogProvider.pinDialog.clear()
        dialogProvider.passwordDialog.clear()
        dialogProvider.biom.clear()
        forgotPasswordActionEnable = false
        forgotPasswordAction = {}
    }

    fun setCheckRefreshError(
        check: Boolean = false, errorStatus: E? = null, useExtraCheck: Boolean = false, extraCheck: (BaseErrorModel<E>) -> Unit = {}
    ) {
        checkRefreshError = check
        refresherErrorStatusToCheck = errorStatus
        this.useExtraCheck = useExtraCheck
        extraRefreshCheck = extraCheck
    }

}