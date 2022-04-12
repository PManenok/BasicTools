/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

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
import by.esas.tools.logger.ILogger.Companion.CATEGORY_ERROR
import java.lang.ref.WeakReference
import javax.crypto.Cipher
import javax.crypto.SecretKey

open class Refresher<E : Enum<E>, M : BaseErrorModel<E>>(
    protected val logger: ILogger<E, M>,
    protected val executor: IContainerExecutor<E, M>,
    protected val typeManager: ITypeManager,
    protected val userInfo: BiometricUserInfo,
    protected val mapper: IErrorMapper<E, M>,
    protected val dialogProvider: DialogProvider,
    protected val supporter: Supporter<E, M>
) : IRefreshContainer<E, M> {
    val TAG: String = Refresher::class.java.simpleName
    protected var accessToken: String = ""
    protected var refreshToken: String = ""

    // default cancellation callback
    protected val defaultCancellationCallback: IContainerCancellationCallback = object :
        IContainerCancellationCallback {
        override fun onCancel() {
            logger.logInfo("defaultCancellationCallback onCancel does nothing")
            //does nothing. should be set before refresh usage
        }
    }
    protected var containerCancellationCallback: IContainerCancellationCallback =
        defaultCancellationCallback // cancellation callback invoke if UC callback not set?

    protected var fragmentManager: WeakReference<FragmentManager?> =
        WeakReference(null) // fragment manager for simple dialogs
    protected var activity: WeakReference<FragmentActivity?> = WeakReference(null) // activity for biometric dialog

    protected var result: ContainerRequest<RefreshResult, E, M> =
        ContainerRequest<RefreshResult, E, M>() // result instance which will get result when it is finished
    protected var secretResult: ContainerRequest<String, E, M> =
        ContainerRequest<String, E, M>() // result instance which will get result when it is finished
    protected var userIdValue: String = "" // user ID sets from getSecrets or with setUserId method

    protected var isBiometricAvailable: Boolean = false // shows if biometric is available
    protected var needCheck: Boolean = false // enable check scenario
    protected var needSecret: Boolean = false // enable secret scenario
    protected var refreshExplicitly: Boolean = false // enable refresh in check access

    //private var presentType: AuthType = AuthType.NONE // secret type that user uses at the moment

    protected var stateIsPaused: Boolean = false // app is in pause
    protected var lastAction: () -> Unit = {} // last action which will be invoked after resume

    protected var currentDialog: IBaseDialog? = null // current(last) simple dialog
    protected var currentBiometricDialog: IBiometric? = null // current(last) biometric dialog

    protected var forgotPasswordActionEnable: Boolean = false // forgot password Flag
    protected var forgotPasswordAction: () -> Unit = {} // action for forgot password

    protected val types: MutableSet<AuthType> = mutableSetOf()// shows which secret types user has

    protected var refresherErrorStatusToCheck: E? = null
    protected var checkRefreshError: Boolean = false
    protected var useExtraCheck: Boolean = false
    protected var extraRefreshCheck: (BaseErrorModel<E>) -> Unit = {}

    init {
        logger.setTag(TAG)
    }

    override fun setUserId(userId: String) {
        this.userIdValue = userId
    }

    override fun getToken(): String {
        logger.logOrder("getToken()")
        return accessToken
    }

    override fun setToken(token: Token) {
        logger.logOrder("setToken() refreshToken isNullOrBlank = ${token.refreshToken.isNullOrBlank()}")
        accessToken = token.accessToken
        refreshToken = token.refreshToken ?: ""
    }

    override fun getRefresh(): String {
        return refreshToken
    }

    /**
     * Запуск запроса на восстановление маркера доступа с последующим обновлением
     * данных контейнера и выполнением переданного блока кода
     */
    override fun refresh(onComplete: (String?) -> Unit, onError: (BaseErrorModel<E>) -> Unit, onCancel: () -> Unit) {
        logger.logOrder("refresh")
        this.refreshExplicitly = true
        result = ContainerRequest<RefreshResult, E, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.logOrder("refresh result.onComplete token != null = ${tokenResponse.token != null}")
                    tokenResponse.token?.let { setToken(it) }
                    onComplete(tokenResponse.token?.accessToken)
                }
                onError {
                    logger.logOrder("refresh result.onError")
                    logger.logError(it)
                    onError(it)
                }
                onCancel {
                    logger.logOrder("refresh result.onCancel")
                    onCancel()
                }
            }

        logger.logDebug("refresh needCheck = false")
        needCheck = false
        needSecret = false
        if (refreshToken.isNotEmpty()) {
            logger.logDebug("refresh refreshToken isNotEmpty")
            refreshAccess(refreshToken)
        } else {
            logger.logDebug("refresh refreshToken isEmpty")
            userIdValue = userInfo.getCurrentUser()
            logger.logUserInfo("refresh userId = $userIdValue")
            getSecrets()
        }
    }

    override fun onCancel() {
        logger.logOrder("onCancel")
        containerCancellationCallback.onCancel()
    }

    override fun checkAccess(refreshExplicitly: Boolean, response: ContainerRequest<String, E, M>.() -> Unit) {
        logger.logOrder("checkAccess refreshExplicitly = $refreshExplicitly")
        this.refreshExplicitly = refreshExplicitly
        val result = ContainerRequest<String, E, M>().apply(response)

        this.result = ContainerRequest<RefreshResult, E, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.logOrder("checkAccess result.onComplete hasAccess = ${tokenResponse.hasAccess}")
                    if (tokenResponse.hasAccess) {
                        logger.logDebug("checkAccess result.onComplete token != null = ${tokenResponse.token != null}")
                        tokenResponse.token?.let { token ->
                            setToken(token)
                        }
                        result(accessToken)
                    } else result()
                }
                onError {
                    logger.logOrder("checkAccess result.onError")
                    logger.logError(it)
                    result(it)
                }
                onCancel {
                    logger.logOrder("checkAccess result.onCancel")
                    result()
                }
            }
        logger.logDebug("checkAccess needCheck = true needSecret = false")
        needCheck = true
        needSecret = false
        userIdValue = userInfo.getCurrentUser()
        logger.logUserInfo("checkAccess userId = $userIdValue")
        getSecrets()
    }

    override fun getSecret(response: ContainerRequest<String, E, M>.() -> Unit) {
        logger.logOrder("getSecret")
        val result = ContainerRequest<String, E, M>().apply(response)

        this.secretResult = result
        logger.logDebug("getSecret needSecret = true, needCheck = false")
        needSecret = true
        needCheck = false
        userIdValue = userInfo.getCurrentUser()
        logger.logUserInfo("getSecret userId = $userIdValue")
        getSecrets()
    }

    override fun saveRefresh(refreshToken: String, response: ContainerRequest<String, E, M>.() -> Unit) {
        val result = ContainerRequest<String, E, M>().apply(response)

        this.result = ContainerRequest<RefreshResult, E, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.logOrder("saveRefresh result.onComplete")
                    result(accessToken)
                }
                onError {
                    logger.logOrder("saveRefresh result.onError")
                    logger.logError(it)
                    result(it)
                }
                onCancel {
                    logger.logOrder("saveRefresh result.onCancel")
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

    protected open fun refreshAccess(refreshToken: String) {
        logger.logOrder("refreshAccess")
        executor.refreshAccessUC(refreshToken) {
            onComplete { token ->
                logger.logOrder("refreshAccess onComplete it != null ${token != null}")
                if (token != null) {
                    result(RefreshResult(token = token))
                }
            }
            onError { error ->
                logger.logOrder("refreshAccess onError error = $error")
                if (checkRefreshError) {
                    checkRefreshError(error)
                } else {
                    result(error)
                }
            }
        }
    }

    protected open fun checkRefreshError(error: M) {
        logger.logOrder("checkRefreshError")
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

    protected open fun getSecrets() {
        logger.logOrder("getSecrets")
        types.clear()
        executor.getSecretsUC {
            onComplete {
                logger.logOrder("getSecrets onComplete types not empty = ${it.isNotEmpty()}")
                if (it.isNotEmpty()) {
                    doWhenHasSecrets(it)
                } else {
                    if (needSecret)
                        secretResult(supporter.util.createErrorModel(0, ErrorStatusEnum.HAS_NO_SECRETS.name))
                    else
                        showPasswordDialog()
                }
            }
            onError { throwable ->
                logger.logOrder("getSecrets onError")
                result(throwable)
            }
        }
    }

    protected open fun doWhenHasSecrets(list: List<AuthType>) {
        types.addAll(list)
        logger.logOrder("getSecrets onComplete onlyOnePresent = ${list.size == 1}")
        val preferred = typeManager.getPreferredType()
        logger.logDebug("getSecrets onComplete preferred = $preferred")
        if (preferred == AuthType.NONE || types.contains(preferred)) {
            when (preferred) {
                AuthType.PIN_AUTH -> {
                    showDecryptPinDialog()
                }
                AuthType.BIOMETRIC_AUTH -> {
                    showDecryptBiometricDialog()
                }
                AuthType.NONE -> {
                    if (needSecret) {
                        secretResult(
                            supporter.util.createErrorModel(0, ErrorStatusEnum.HAS_NO_SECRETS.name)
                        )
                    } else {
                        showPasswordDialog()
                    }
                }
            }
        } else {
            showAuthDialog(isDecrypting = true)
        }
    }

/*############################# Refresh token with dialogs END ##############################*/

/*############################# Check access with dialogs ###################################*/

    protected open fun checkSecret(pinKey: SecretKey? = null, cipher: Cipher? = null) {
        logger.logOrder("checkSecret pinKey!=null ${pinKey != null}, cipher!=null ${cipher != null}")
        executor.checkSecretUC(pinKey, cipher) {
            onComplete {
                logger.logOrder("checkSecret onComplete ${it.first}")
                if (it.first) {
                    when {
                        needSecret -> secretResult(it.second)
                        refreshExplicitly -> {
                            logger.logDebug("checkSecret onComplete refreshToken != null")
                            refreshWithSecret(it.second)
                        }
                        else -> {
                            logger.logDebug("checkSecret onComplete return")
                            result(RefreshResult())
                        }
                    }
                } else {
                    logger.logDebug("checkSecret onComplete failed check return error SECRET_CHECK_NOT_MATCH")
                    val error = supporter.util.createErrorModel(0, ErrorStatusEnum.SECRET_CHECK_NOT_MATCH.name)
                    if (needSecret) secretResult(error)
                    else result(error)
                }
            }
            onError { error ->
                logger.logOrder("checkSecret onError")
                result(error)
            }
        }
    }

    protected open fun refreshWithSecret(secret: String) {
        logger.logOrder("refreshWithSecret")
        executor.refreshWithSecretUC(secret) {
            onComplete { token ->
                logger.logOrder("refreshWithSecret onComplete")
                result(RefreshResult(token))
            }

            onError { error ->
                logger.logOrder("refreshWithSecret onError")
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
    protected open fun authenticate(password: String, recreate: Boolean, noSecrets: Boolean) {
        logger.logOrder("authenticate password not blank = ${password.isNotBlank()}; recreate = $recreate; noSecrets = $noSecrets")
        if (password.isNotBlank()) {
            executor.authorizationInSystemUC(password) {
                onComplete { token ->
                    logger.logOrder("authenticate onComplete it.refreshToken != null ${token.refreshToken != null}")
                    if (token.refreshToken != null) {
                        logger.logDebug("authenticate onComplete isBiometricAvailable $isBiometricAvailable")

                        if (recreate) {
                            if (isBiometricAvailable) {
                                showEncryptBiometricDialog(token)
                            } else {
                                showEncryptPinDialog(token)
                            }
                        } else {
                            result(RefreshResult(token))
                        }
                    } else {
                        logger.logDebug("authenticate onComplete return")
                        result(RefreshResult(token))
                    }
                }
                onError { error ->
                    logger.logOrder("authenticate onError")
                    result(error)
                }
            }
        } else {
            logger.logDebug("authenticate showPasswordDialog")
            showPasswordDialog(noSecrets)
        }
    }

    protected open fun putSecrets(token: Token, cipher: Cipher? = null, pinKey: SecretKey? = null) {
        logger.logOrder("putSecrets pinKey!=null ${pinKey != null}, cipher!=null ${cipher != null}")
        executor.createSecretUC(token.refreshToken!!, cipher, pinKey) {
            onComplete {
                logger.logOrder("putSecrets onComplete return")
                result(RefreshResult(token))
            }
            onError {
                logger.logOrder("putSecrets onError return")
                logger.showMessage("Secret was not saved") //todo show message secret was not create
                result(RefreshResult(token))
            }
        }
    }
/*############################## Authentication with password END ###########################*/

/*############################### Dialogs ###################################################*/

    protected open fun showAuthDialog(isDecrypting: Boolean, token: Token? = null) {
        logger.logOrder("showAuthDialog isDecrypting ${isDecrypting}, token!=null = ${token != null}")
        dialogProvider.menuDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e ->
                logger.logOrder("showAuthDialog onError ${e.message}")
                if (needSecret) secretResult(mapper(e))
                else result(mapper(e))
            }, { afterOk ->
                logger.logOrder("showAuthDialog onDismiss afterOk=${afterOk}")
                if (!afterOk) {
                    if (needSecret) secretResult()
                    else result()
                }
            })
            dialog.isDecrypting(isDecrypting)
            dialog.showPassword(!needSecret)
            dialog.setCallBacks({
                logger.logOrder("showAuthDialog onPinClick")
                if (isDecrypting) showDecryptPinDialog()
                else token?.let { showEncryptPinDialog(token) }
            }, {
                logger.logOrder("showAuthDialog onBiometricClick")
                if (isDecrypting) showDecryptBiometricDialog()
                else token?.let { showEncryptBiometricDialog(token) }
            }, {
                logger.logOrder("showAuthDialog onPasswordClick")
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
    protected open fun showDecryptPinDialog() {
        logger.logOrder("showDecryptPinDialog")
        dialogProvider.pinDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e ->
                logger.logOrder("showDecryptPinDialog onError ${e.message}")
                if (needSecret) secretResult(mapper(e))
                else result(mapper(e))
            }, { afterOk ->
                logger.logOrder("showDecryptPinDialog onDismiss afterOk=$afterOk")
                if (!afterOk) {
                    if (needSecret) secretResult()
                    else result()
                }
            })

            dialog.setCallbacks({ pin ->
                logger.logOrder("showDecryptPinDialog onPinComplete needCheck=$needCheck")
                val pinKey = supporter.util.generatePin(
                    pin, userIdValue,
                    Settings.Secure.getString(activity.get()?.contentResolver, Settings.Secure.ANDROID_ID)
                )
                typeManager.putPreferredType(AuthType.PIN_AUTH)
                //if (needCheck) {
                checkSecret(pinKey = pinKey)
                /*} else {
                    refreshWithSecret(pinKey = pinKey)
                }*/
            }, {
                logger.logOrder("showDecryptPinDialog onPinCanceled showAuthDialog(isDecrypting = true)")
                showAuthDialog(isDecrypting = true)
            })

            logger.logDebug("showDecryptPinDialog needCheck=$needCheck")
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

    protected open fun showEncryptPinDialog(token: Token) {
        logger.logOrder("showEncryptPinDialog")
        dialogProvider.pinDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e ->
                logger.logOrder("showDecryptPinDialog onError ${e.message}")
                result(mapper(e))
            }, { afterOk ->
                logger.logOrder("showEncryptPinDialog onDismiss afterOk=$afterOk")
                if (!afterOk) {
                    result(
                        RefreshResult(
                            token
                        )
                    )
                }
            })
            dialog.setCallbacks({ pin ->
                logger.logOrder("showEncryptPinDialog onPinComplete preferredType=PIN_AUTH")
                val pinKey = supporter.util.generatePin(
                    pin, userIdValue,
                    Settings.Secure.getString(activity.get()?.contentResolver, Settings.Secure.ANDROID_ID)
                )
                typeManager.putPreferredType(AuthType.PIN_AUTH)
                putSecrets(token = token, pinKey = pinKey)
            }, {
                logger.logOrder("showEncryptPinDialog onPinCanceled showAuthDialog(isDecrypting = false)")
                showAuthDialog(isDecrypting = false, token = token)
            })

            dialog.setTitle(supporter.resProvider.provideEncryptTitle())
            logger.logDebug("showEncryptPinDialog isBiometricAvailable=$isBiometricAvailable")
            val cancelId = if (isBiometricAvailable) supporter.resProvider.provideAlterCancelStr() else -1
            dialog.setCancelTitle(cancelId)
            dialog.setCancellable(false)
            showDialog(dialog.getDialog(), "OperatorPinEncryptDialog")
        }
    }

/*######################## PIN  END #####################*/

/*###################### Biometric ######################*/

    protected open fun showDecryptBiometricDialog() {
        logger.logOrder("showDecryptBiometricDialog")
        dialogProvider.biom.let { dialog ->
            activity.get()?.let { context ->
                logger.logDebug("showDecryptBiometricDialog activity let")
                dialog.onAuthenticationCallback({ isCancelled, errString ->
                    logger.logOrder("Decrypt biometric dialog onAuthenticationCallback onError")
                    logger.logDebug("onError isCancelled=$isCancelled errString=$errString")
                    context.runOnUiThread {
                        if (isCancelled) {//Операция с отпечатком отменена. on pause
                            logger.logDebug("$errString isPaused = $stateIsPaused")
                            if (!stateIsPaused) { // somehow dialog set error_canceled after resume if it was unblocked not with finger
                                logger.logDebug("Show decrypt biometric dialog again")
                                showDecryptBiometricDialog()
                            } else {
                                logger.logDebug("Set last action to show decrypt biometric dialog")
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
                    logger.logOrder("DecryptBiometricDialog preferredType BIOMETRIC_AUTH")
                    typeManager.putPreferredType(AuthType.BIOMETRIC_AUTH)
                    logger.logDebug("DecryptBiometricDialog needCheck $needCheck")
                    //if (needCheck) {
                    checkSecret(cipher = cipher)
                    /*} else {
                        refreshWithSecret(cipher = cipher)
                    }*/
                })

                logger.logDebug("showDecryptBiometricDialog needCheck=$needCheck")

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
                    logger.logOrder("showDecryptBiometricDialog lastAction DECRYPT_MODE")
                    try {
                        currentBiometricDialog = dialog.authenticate(isDecrypt = true)
                    } catch (e: Exception) {
                        logger.logOrder("showDecryptBiometricDialog catch exception")
                        logger.logError(e)
                        deleteBiometricType()
                    }
                }
                logger.logDebug("showDecryptBiometricDialog stateIsPaused=$stateIsPaused")
                if (!stateIsPaused) {
                    lastAction.invoke()
                    lastAction = {}
                }
            }
        }
    }

    protected open fun showEncryptBiometricDialog(token: Token) {
        logger.logOrder("showEncryptBiometricDialog")

        dialogProvider.biom.let { dialog ->
            activity.get()?.let { context ->
                logger.logOrder("showDecryptBiometricDialog activity let")
                dialog.onAuthenticationCallback({ isCancelled, errString ->
                    logger.logOrder("Encrypt biometric dialog onAuthenticationCallback onError")
                    logger.logDebug("onError isCancelled=$isCancelled errString=$errString")
                    context.runOnUiThread {
                        if (isCancelled) {//Операция с отпечатком отменена. on pause
                            logger.logDebug("showEncryptBiometricDialog ERROR_CANCELED")
                            lastAction = { showEncryptBiometricDialog(token) }
                        } else {
                            logger.logDebug("showEncryptBiometricDialog showAuthDialog(isDecrypting = false,token)")
                            showAuthDialog(isDecrypting = false, token = token)
                        }
                    }
                }, { cipher ->
                    logger.logOrder("EncryptBiometricDialog Success preferredType BIOMETRIC_AUTH")
                    typeManager.putPreferredType(AuthType.BIOMETRIC_AUTH)
                    putSecrets(token = token, cipher = cipher)
                })


                dialog.create(context, userInfo)
                val title: String = supporter.resProvider.getString(supporter.resProvider.provideEncryptTitle())
                val negativeText = supporter.resProvider.getString(supporter.resProvider.provideAlterCancelStr())
                dialog.setInfo(title, negativeText)
                logger.logDebug("showEncryptBiometricDialog stateIsPaused=$stateIsPaused")
                lastAction = {
                    logger.logOrder("showEncryptBiometricDialog lastAction ENCRYPT_MODE")
                    try {
                        currentBiometricDialog = dialog.authenticate(isDecrypt = false)
                    } catch (e: Exception) {
                        logger.logOrder("showEncryptBiometricDialog catch exception")
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

    protected open fun showPasswordDialog(noSecrets: Boolean = false) {
        logger.logOrder("showPasswordDialog")
        dialogProvider.passwordDialog.let { dialog ->
            dialog.createDialog()
            dialog.setStateActions({ e: Exception ->
                logger.logOrder("showPasswordDialog onError ${e.message}")
                result(mapper(e))
            }, { afterOk: Boolean ->
                logger.logOrder("showPasswordDialog onDismiss afterOk=$afterOk, types not empty=${types.isNotEmpty()}")
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
            logger.logDebug("showPasswordDialog types not empty=${types.isNotEmpty()}")
            if (!noSecrets && types.isNotEmpty()) {
                dialog.setCancelTitle(supporter.resProvider.provideAlterCancelStr())
                dialog.setShowRecreateAuth(true)
            }
            logger.logDebug("showPasswordDialog forgotPasswordActionEnable=$forgotPasswordActionEnable")
            dialog.setForgotPassword(forgotPasswordActionEnable)
            logger.logDebug("showPasswordDialog needCheck=$needCheck")
            if (needCheck)
                dialog.setTitle(supporter.resProvider.provideAccessConfirmStr())
            else
                dialog.setTitle(supporter.resProvider.provideAccessStr())
            dialog.setCallbacks({ password: String, recreate: Boolean ->
                logger.logOrder("showPasswordDialog onPasswordComplete")
                authenticate(password, recreate, noSecrets)
            }, {
                logger.logOrder("showPasswordDialog onPasswordForgot forgotPasswordActionEnable=$forgotPasswordActionEnable")
                if (forgotPasswordActionEnable)
                    forgotPasswordAction()
            })

            showDialog(dialog.getDialog(), "PasswordDialog")
        }
    }


/*##################### Password END ####################*/

/*################################## Dialogs END ############################################*/

/*################################## Common #################################################*/

    protected open fun showDialog(dialog: IBaseDialog, tag: String) {
        logger.logOrder("showDialog $tag stateIsPaused=$stateIsPaused")
        lastAction = {
            logger.logOrder("showDialog lastAction invoke")
            val manager = fragmentManager.get()
            val context = activity.get()
            logger.logDebug("showDialog lastAction fragmentManager != null ${manager != null} context != null ${context != null}")
            currentDialog = dialog
            dialog.show(manager, context, tag)
        }
        if (!stateIsPaused) {
            lastAction.invoke()
            lastAction = {}
        }
    }

    protected open fun deleteBiometricType() {
        logger.logOrder("deleteBiometricType BIOMETRIC_AUTH")
        executor.deleteSecretTypeUC(AuthType.BIOMETRIC_AUTH) {
            onComplete {
                logger.logOrder("deleteBiometricType $it")
                if (it) {
                    types.remove(AuthType.BIOMETRIC_AUTH)
                    if (types.isEmpty()) {
                        val presentType = AuthType.NONE
                        logger.logDebug("deleteBiometricType containsSecrets = false presentType=NONE")
                        showPasswordDialog()
                    } else {
                        val presentType = types.firstOrNull() ?: AuthType.NONE
                        typeManager.putPreferredType(presentType)
                        logger.logDebug("deleteBiometricType presentType=$presentType")
                        showAuthDialog(isDecrypting = true)
                    }
                }
            }
            onError {
                logger.logError(it)
                logger.logOrder("deleteBiometricType showAuthDialog(isDecrypting = true)")
                showAuthDialog(isDecrypting = true)
            }
        }
    }
/*#################################### Common END ###########################################*/

/*############################## Settings ###################################################*/
    /**
     * Установка Activity необходимая для работы диалогов
     *
     * @param activity FragmentActivity
     */
    override fun setActivity(activity: FragmentActivity) {
        logger.logOrder("setActivity")
        this.activity = WeakReference(activity)
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun doAfterResume() {
                logger.logOrder("activity LifecycleObserver doAfterResume")
                stateIsPaused = false
                lastAction.invoke()
                lastAction = {}
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun doAfterPause() {
                logger.logOrder("activity LifecycleObserver doAfterPause")
                stateIsPaused = true
            }
        })
        isBiometricAvailable =
            supporter.util.checkBiometricSupport(activity.applicationContext) { tag: String, msg: String ->
                logger.logCategory(CATEGORY_ERROR, tag, msg)
            }
        logger.logDebug("setActivity isBiometricAvailable=$isBiometricAvailable")
    }

    override fun setForgotPasswordAction(enable: Boolean, forgotPasswordAction: () -> Unit) {
        logger.logOrder("setForgotPasswordAction enable=$enable")
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
        logger.logOrder("setParams")
        fragmentManager = WeakReference(manager)
    }

    override fun setCancellationCallback(callback: IContainerCancellationCallback) {
        logger.logOrder("setCancellationCallback")
        containerCancellationCallback = callback
    }

/*############################## Settings END ###############################################*/

    override fun clearAccess() {
        logger.logOrder("clearAccess")
        accessToken = ""
        refreshToken = ""
        executor.unsubscribe()
    }

    override fun clear() {
        logger.logOrder("clear")
        clearAccess()
        activity.clear()
        fragmentManager.clear()
        containerCancellationCallback = defaultCancellationCallback
        result = ContainerRequest()
        userIdValue = ""
        isBiometricAvailable = false
        needCheck = false
        refreshExplicitly = false
        types.clear()
        //presentType = AuthType.NONE
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

    open fun setCheckRefreshError(
        check: Boolean = false,
        errorStatus: E? = null,
        useExtraCheck: Boolean = false,
        extraCheck: (BaseErrorModel<E>) -> Unit = {}
    ) {
        checkRefreshError = check
        refresherErrorStatusToCheck = errorStatus
        this.useExtraCheck = useExtraCheck
        extraRefreshCheck = extraCheck
    }

}