/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer

import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import by.esas.tools.accesscontainer.dialog.BiometricUserInfo
import by.esas.tools.accesscontainer.dialog.DialogProvider
import by.esas.tools.accesscontainer.dialog.IBaseDialog
import by.esas.tools.accesscontainer.dialog.IBiometric
import by.esas.tools.accesscontainer.dialog.setters.MenuResultHandler
import by.esas.tools.accesscontainer.dialog.setters.PasswordResultHandler
import by.esas.tools.accesscontainer.dialog.setters.PinResultHandler
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

open class Refresher<M : BaseErrorModel>(
    protected val logger: ILogger<M>,
    protected val executor: IContainerExecutor<M>,
    protected val typeManager: ITypeManager,
    protected val userInfo: BiometricUserInfo,
    protected val mapper: IErrorMapper<M>,
    protected val dialogProvider: DialogProvider,
    protected val supporter: Supporter
) : IRefreshContainer<M> {

    val TAG: String = Refresher::class.java.simpleName

    /*region Params*/

    protected var accessToken: String = ""
    protected var refreshToken: String = ""

    // default cancellation callback
    protected val defaultCancellationCallback: IContainerCancellationCallback = object :
        IContainerCancellationCallback {
        override fun onCancel() {
            logger.i(TAG, "defaultCancellationCallback onCancel does nothing")
            //does nothing. should be set before refresh usage
        }
    }
    protected var containerCancellationCallback: IContainerCancellationCallback =
        defaultCancellationCallback // cancellation callback invoke if UC callback not set?

    /*protected var fragmentManager: WeakReference<FragmentManager?> =
        WeakReference(null) // fragment manager for simple dialogs*/
    protected var activity: WeakReference<FragmentActivity?> =
        WeakReference(null) // activity for biometric dialog

    protected var result: ContainerRequest<RefreshResult, M> =
        ContainerRequest<RefreshResult, M>() // result instance which will get result when it is finished
    protected var secretResult: ContainerRequest<String, M> =
        ContainerRequest<String, M>() // result instance which will get result when it is finished
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

    protected var refresherErrorStatusToCheck: String? = null
    protected var checkRefreshError: Boolean = false
    protected var useExtraCheck: Boolean = false
    protected var extraRefreshCheck: (BaseErrorModel) -> Unit = {}

    /*endregion Params*/

    /*region ############################# Base functionality ##########################################*/

    override fun setUserId(userId: String) {
        this.userIdValue = userId
    }

    override fun getToken(): String {
        logger.order(TAG, "getToken()")
        return accessToken
    }

    override fun setToken(token: Token) {
        logger.order(
            TAG,
            "setToken() refreshToken isNullOrBlank = ${token.refreshToken.isNullOrBlank()}"
        )
        accessToken = token.accessToken
        refreshToken = token.refreshToken ?: ""
    }

    override fun getRefresh(): String {
        return refreshToken
    }

    /**
     * Initiate a request to restore an access token, then update
     * the container data and execute the passed block of code
     */
    override fun refresh(
        onComplete: (String?) -> Unit,
        onError: (M) -> Unit,
        onCancel: () -> Unit
    ) {
        logger.order(TAG, "refresh")
        this.refreshExplicitly = true
        result = ContainerRequest<RefreshResult, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.order(
                        TAG,
                        "refresh result.onComplete token != null = ${tokenResponse.token != null}"
                    )
                    tokenResponse.token?.let { setToken(it) }
                    onComplete(tokenResponse.token?.accessToken)
                }
                onError {
                    logger.order(TAG, "refresh result.onError")
                    logger.errorModel(TAG, it)
                    onError(it)
                }
                onCancel {
                    logger.order(TAG, "refresh result.onCancel")
                    onCancel()
                }
            }

        logger.d(TAG, "refresh needCheck = false")
        needCheck = false
        needSecret = false
        if (refreshToken.isNotEmpty()) {
            logger.d(TAG, "refresh refreshToken isNotEmpty")
            refreshAccess(refreshToken)
        } else {
            logger.d(TAG, "refresh refreshToken isEmpty")
            userIdValue = userInfo.getCurrentUser()
            logger.userInfo(TAG, "refresh userId = $userIdValue")
            getSecrets()
        }
    }

    override fun onCancel() {
        logger.order(TAG, "onCancel")
        containerCancellationCallback.onCancel()
    }

    override fun checkAccess(
        refreshExplicitly: Boolean,
        response: ContainerRequest<String, M>.() -> Unit
    ) {
        logger.order(TAG, "checkAccess refreshExplicitly = $refreshExplicitly")
        this.refreshExplicitly = refreshExplicitly
        val result = ContainerRequest<String, M>().apply(response)

        this.result = ContainerRequest<RefreshResult, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.order(
                        TAG,
                        "checkAccess result.onComplete hasAccess = ${tokenResponse.hasAccess}"
                    )
                    if (tokenResponse.hasAccess) {
                        logger.d(
                            TAG,
                            "checkAccess result.onComplete token != null = ${tokenResponse.token != null}"
                        )
                        tokenResponse.token?.let { token ->
                            setToken(token)
                        }
                        result(accessToken)
                    } else result()
                }
                onError {
                    logger.order(TAG, "checkAccess result.onError")
                    logger.errorModel(TAG, it)
                    result(it)
                }
                onCancel {
                    logger.order(TAG, "checkAccess result.onCancel")
                    result()
                }
            }
        logger.d(TAG, "checkAccess needCheck = true needSecret = false")
        needCheck = true
        needSecret = false
        userIdValue = userInfo.getCurrentUser()
        logger.userInfo(TAG, "checkAccess userId = $userIdValue")
        getSecrets()
    }

    override fun getSecret(response: ContainerRequest<String, M>.() -> Unit) {
        logger.order(TAG, "getSecret")
        val result = ContainerRequest<String, M>().apply(response)

        this.secretResult = result
        logger.d(TAG, "getSecret needSecret = true, needCheck = false")
        needSecret = true
        needCheck = false
        userIdValue = userInfo.getCurrentUser()
        logger.userInfo(TAG, "getSecret userId = $userIdValue")
        getSecrets()
    }

    override fun saveRefresh(
        refreshToken: String,
        response: ContainerRequest<String, M>.() -> Unit
    ) {
        val result = ContainerRequest<String, M>().apply(response)

        this.result = ContainerRequest<RefreshResult, M>()
            .apply {
                onComplete { tokenResponse ->
                    logger.order(TAG, "saveRefresh result.onComplete")
                    this@Refresher.refreshToken = tokenResponse.token?.refreshToken ?: ""
                    result(accessToken)
                }
                onError {
                    logger.order(TAG, "saveRefresh result.onError")
                    logger.errorModel(TAG, it)
                    result(it)
                }
                onCancel {
                    logger.order(TAG, "saveRefresh result.onCancel")
                    result()
                }
            }

        if (isBiometricAvailable) {
            showEncryptBiometricDialog(Token(accessToken, refreshToken))
        } else {
            showEncryptPinDialog(Token(accessToken, refreshToken))
        }
    }

    override fun clearAccess() {
        logger.order(TAG, "clearAccess")
        accessToken = ""
        refreshToken = ""
        executor.unsubscribe()
    }

    override fun clear() {
        logger.order(TAG, "clear")
        clearAccess()
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
        dialogProvider.menuDialog.clear(activity.get())
        dialogProvider.pinDialog.clear(activity.get())
        dialogProvider.passwordDialog.clear(activity.get())
        dialogProvider.biom.clear()
        activity.clear()
        forgotPasswordActionEnable = false
        forgotPasswordAction = {}
    }

    open fun setCheckRefreshError(
        check: Boolean = false,
        errorStatus: String? = null,
        useExtraCheck: Boolean = false,
        extraCheck: (BaseErrorModel) -> Unit = {}
    ) {
        checkRefreshError = check
        refresherErrorStatusToCheck = errorStatus
        this.useExtraCheck = useExtraCheck
        extraRefreshCheck = extraCheck
    }

    /*endregion ########################### Base functionality #########################################*/

    /*region ############################ Simple refresh token #########################################*/

    protected open fun refreshAccess(refreshToken: String) {
        logger.order(TAG, "refreshAccess")
        executor.refreshAccessUC(refreshToken) {
            onComplete { token ->
                logger.order(TAG, "refreshAccess onComplete it != null ${token != null}")
                if (token != null) {
                    result(RefreshResult(token = token))
                }
            }
            onError { error ->
                logger.order(TAG, "refreshAccess onError error = $error")
                if (checkRefreshError) {
                    checkRefreshError(error)
                } else {
                    result(error)
                }
            }
        }
    }

    protected open fun checkRefreshError(error: M) {
        logger.order(TAG, "checkRefreshError")
        if (useExtraCheck) {
            extraRefreshCheck(error)
        } else {
            if (error.status == refresherErrorStatusToCheck) {
                //deleteSecrets()
                showPasswordDialog(true)
            } else
                result(error)
        }
    }

    /*endregion ############################# Simple refresh token END ####################################*/

    /*region ############################# Refresh token with dialogs ##################################*/

    protected open fun getSecrets() {
        logger.order(TAG, "getSecrets")
        types.clear()
        executor.getSecretsUC {
            onComplete {
                logger.order(TAG, "getSecrets onComplete types not empty = ${it.isNotEmpty()}")
                if (it.isNotEmpty()) {
                    doWhenHasSecrets(it)
                } else {
                    if (needSecret)
                        secretResult(mapper(0, ErrorStatusEnum.HAS_NO_SECRETS.name))
                    else
                        showPasswordDialog()
                }
            }
            onError { throwable ->
                logger.order(TAG, "getSecrets onError")
                result(throwable)
            }
        }
    }

    protected fun showDialogForType(type: AuthType) {
        when (type) {
            AuthType.PIN_AUTH -> {
                showDecryptPinDialog()
            }

            AuthType.BIOMETRIC_AUTH -> {
                showDecryptBiometricDialog()
            }

            AuthType.NONE -> {
                showPasswordDialog()
            }
        }
    }

    protected open fun doWhenHasSecrets(list: List<AuthType>) {
        types.clear()
        types.addAll(list)
        logger.order(TAG, "getSecrets onComplete onlyOnePresent = ${list.size == 1}")
        val preferred = typeManager.getPreferredType()
        logger.d(TAG, "getSecrets onComplete preferred = $preferred")

        if (!needSecret) {
            //needSecret != true
            if (preferred == AuthType.NONE || list.contains(preferred)) {
                showDialogForType(preferred)
            } else if (list.size > 1 || !list.contains(AuthType.NONE)) {
                //FIX
                //keep in mind that list can not be empty
                showAuthDialog(isDecrypting = true)
            } else {
                showDialogForType(list[0])
            }
        } else {
            //needSecret == true
            if (preferred != AuthType.NONE && list.contains(preferred)) {
                //preferred is not NONE and List contains preferred
                showDialogForType(preferred)
            } else {
                //preferred is NONE or list does not contain preferred
                val filtered = list.filter { it != AuthType.NONE }
                if (filtered.isEmpty()) {
                    secretResult(mapper(0, ErrorStatusEnum.HAS_NO_SECRETS.name))
                } else if (filtered.size == 1) {
                    showDialogForType(filtered[0])
                } else {
                    showAuthDialog(isDecrypting = true)
                }
            }
        }
    }

    /*endregion ############################# Refresh token with dialogs END ##############################*/

    /*region ############################# Check access with dialogs ###################################*/

    protected open fun checkSecret(pinKey: SecretKey? = null, cipher: Cipher? = null) {
        logger.order(
            TAG,
            "checkSecret pinKey!=null ${pinKey != null}, cipher!=null ${cipher != null}"
        )
        executor.checkSecretUC(pinKey, cipher) {
            onComplete {
                logger.order(TAG, "checkSecret onComplete ${it.first}")
                if (it.first) {
                    when {
                        needSecret -> secretResult(it.second)
                        refreshExplicitly -> {
                            logger.d(TAG, "checkSecret onComplete refreshToken != null")
                            refreshWithSecret(it.second)
                        }

                        else -> {
                            logger.d(TAG, "checkSecret onComplete return")
                            result(RefreshResult())
                        }
                    }
                } else {
                    logger.d(
                        TAG,
                        "checkSecret onComplete failed check return error SECRET_CHECK_NOT_MATCH"
                    )
                    val error = mapper(0, ErrorStatusEnum.SECRET_CHECK_NOT_MATCH.name)
                    if (needSecret) secretResult(error)
                    else result(error)
                }
            }
            onError { error ->
                logger.order(TAG, "checkSecret onError")
                result(error)
            }
        }
    }

    protected open fun refreshWithSecret(secret: String) {
        logger.order(TAG, "refreshWithSecret")
        executor.refreshWithSecretUC(secret) {
            onComplete { token ->
                logger.order(TAG, "refreshWithSecret onComplete")
                result(RefreshResult(token))
            }

            onError { error ->
                logger.order(TAG, "refreshWithSecret onError")
                if (checkRefreshError) {
                    checkRefreshError(error)
                } else {
                    result(error)
                }
            }
        }
    }

    /*endregion ############################## Check access with dialogs END ##############################*/

    /*region ############################### Authentication with password ##############################*/
    protected open fun authenticate(password: String, recreate: Boolean, noSecrets: Boolean) {
        logger.order(
            TAG,
            "authenticate password not blank = ${password.isNotBlank()}; recreate = $recreate; noSecrets = $noSecrets"
        )
        if (password.isNotBlank()) {
            executor.authorizationInSystemUC(password) {
                onComplete { token ->
                    logger.order(
                        TAG,
                        "authenticate onComplete it.refreshToken != null ${token.refreshToken != null}"
                    )
                    if (token.refreshToken != null) {
                        logger.d(
                            TAG,
                            "authenticate onComplete isBiometricAvailable $isBiometricAvailable"
                        )

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
                        logger.d(TAG, "authenticate onComplete return")
                        result(RefreshResult(token))
                    }
                }
                onError { error ->
                    logger.order(TAG, "authenticate onError")
                    result(error)
                }
            }
        } else {
            logger.d(TAG, "authenticate showPasswordDialog")
            showPasswordDialog(noSecrets)
        }
    }

    protected open fun putSecrets(token: Token, cipher: Cipher? = null, pinKey: SecretKey? = null) {
        logger.order(
            TAG,
            "putSecrets pinKey!=null ${pinKey != null}, cipher!=null ${cipher != null}"
        )
        executor.createSecretUC(token.refreshToken!!, cipher, pinKey) {
            onComplete {
                logger.order(TAG, "putSecrets onComplete return")
                result(RefreshResult(token))
            }
            onError {
                logger.order(TAG, "putSecrets onError return")
                logger.i(TAG, "Secret was not saved") //todo show message secret was not create
                result(RefreshResult(token))
            }
        }
    }
    /*endregion ############################## Authentication with password END ###########################*/

    /*############################### Dialogs ###################################################*/

    /*region ############################### Menu dialog ##############################################*/

    protected open fun showAuthDialog(isDecrypting: Boolean, token: Token? = null) {
        logger.order(
            TAG,
            "showAuthDialog isDecrypting ${isDecrypting}, token!=null = ${token != null}"
        )
        dialogProvider.menuDialog.let { dialog ->
            dialog.createDialog()
            //setup menu dialog parameters
            val showPin: Boolean
            val showBiom: Boolean
            if (isDecrypting) {
                showPin = types.contains(AuthType.PIN_AUTH)
                showBiom = types.contains(AuthType.BIOMETRIC_AUTH)
            } else {
                showPin = true
                showBiom = isBiometricAvailable
            }
            dialog.setupParameterBundle(
                isDecrypting = isDecrypting,
                showPassword = !needSecret,
                showPin = showPin,
                showBiom = showBiom
            )

            //setup menu dialog result handler
            dialog.setupResultHandler(provideMenuResultHandler(isDecrypting, token))

            val titleRes: Int =
                if (isDecrypting) -1 else supporter.resProvider.provideEncryptTitle()
            dialog.setTitle(titleRes)
            showDialog(dialog.getDialog(), "OperatorAuthDialog")
        }
    }

    protected open fun provideMenuResultHandler(
        decrypting: Boolean,
        accessToken: Token?
    ): MenuResultHandler {
        return object : MenuResultHandler(decrypting, accessToken) {
            override fun onCancel() {
                logger.order(TAG, "showAuthDialog onCancel")
                if (needSecret) secretResult()
                else result()
            }

            override fun onPinClick() {
                logger.order(TAG, "showAuthDialog onPinClick")
                if (this.decrypting) showDecryptPinDialog()
                else this.accessToken?.let { showEncryptPinDialog(it) }
            }

            override fun onPasswordClick() {
                logger.order(TAG, "showAuthDialog onPasswordClick")
                showPasswordDialog()
            }

            override fun onBiomClick() {
                if (this.decrypting) showDecryptBiometricDialog()
                else this.accessToken?.let { showEncryptBiometricDialog(it) }
            }
        }
    }

    /*endregion ############################### Menu dialog ##############################################*/

    /*region ######################## PIN ##########################*/

    protected open fun showDecryptPinDialog() {
        logger.order(TAG, "showDecryptPinDialog")
        dialogProvider.pinDialog.let { dialog ->
            dialog.createDialog()

            val cancelId = supporter.resProvider.provideAlterCancelStr()
            val hasAnother = if (needSecret) types.size > 1 else true
            dialog.setupParameterBundle(cancelId, cancelId, false, hasAnother)

            dialog.setupResultHandler(provideDecryptingPinHandler())

            logger.d(TAG, "showDecryptPinDialog needCheck=$needCheck")
            if (needCheck)
                dialog.setTitle(supporter.resProvider.provideAccessConfirmStr())
            else
                dialog.setTitle(supporter.resProvider.provideAccessStr())
            showDialog(dialog.getDialog(), "OperatorPinDecryptDialog")
        }
    }

    protected open fun showEncryptPinDialog(token: Token) {
        logger.order(TAG, "showEncryptPinDialog")
        dialogProvider.pinDialog.let { dialog ->
            dialog.createDialog()

            logger.d(TAG, "showEncryptPinDialog isBiometricAvailable=$isBiometricAvailable")
            val cancelId =
                if (isBiometricAvailable) supporter.resProvider.provideAlterCancelStr() else -1
            dialog.setupParameterBundle(cancelId, cancelId, false, isBiometricAvailable)

            dialog.setupResultHandler(provideEncryptingPinHandler(token))

            dialog.setTitle(supporter.resProvider.provideEncryptTitle())
            showDialog(dialog.getDialog(), "OperatorPinEncryptDialog")
        }
    }

    protected open fun provideDecryptingPinHandler(): PinResultHandler {
        return object : PinResultHandler(null) {
            override fun onCancel() {
                if (needSecret) secretResult()
                else result()
            }

            override fun onAnotherClicked() {
                logger.order(
                    TAG,
                    "showDecryptPinDialog onPinCanceled showAuthDialog(isDecrypting = true)"
                )
                showAuthDialog(isDecrypting = true)
            }

            override fun onComplete(pin: String) {
                logger.order(TAG, "showDecryptPinDialog onPinComplete needCheck=$needCheck")
                val pinKey = supporter.util.generatePin(
                    pin, userIdValue,
                    Settings.Secure.getString(
                        activity.get()?.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                )
                typeManager.putPreferredType(AuthType.PIN_AUTH)
                checkSecret(pinKey = pinKey)
            }
        }
    }

    protected open fun provideEncryptingPinHandler(token: Token): PinResultHandler {
        return object : PinResultHandler(token) {
            override fun onCancel() {
                result(RefreshResult(this.token))
            }

            override fun onAnotherClicked() {
                logger.order(
                    TAG,
                    "showEncryptPinDialog onPinCanceled showAuthDialog(isDecrypting = false)"
                )
                showAuthDialog(isDecrypting = false, token = this.token)
            }

            override fun onComplete(pin: String) {
                logger.order(TAG, "showEncryptPinDialog onPinComplete preferredType=PIN_AUTH")
                val pinKey = supporter.util.generatePin(
                    pin, userIdValue,
                    Settings.Secure.getString(
                        activity.get()?.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                )
                typeManager.putPreferredType(AuthType.PIN_AUTH)
                this.token?.let { putSecrets(token = it, pinKey = pinKey) }
            }
        }
    }

    /*endregion######################## PIN  END #####################*/

    /*region ###################### Biometric ######################*/

    protected open fun showDecryptBiometricDialog() {
        logger.order(TAG, "showDecryptBiometricDialog")
        dialogProvider.biom.let { dialog ->
            activity.get()?.let { context ->
                logger.d(TAG, "showDecryptBiometricDialog activity let")
                dialog.onAuthenticationCallback({ isCancelled, errString ->
                    logger.order(TAG, "Decrypt biometric dialog onAuthenticationCallback onError")
                    logger.d(TAG, "onError isCancelled=$isCancelled errString=$errString")
                    context.runOnUiThread {
                        if (isCancelled) {//Fingerprint operation cancelled. on pause
                            logger.d(TAG, "$errString isPaused = $stateIsPaused")
                            if (!stateIsPaused) { // somehow dialog set error_canceled after resume if it was unblocked not with finger
                                logger.d(TAG, "Show decrypt biometric dialog again")
                                showDecryptBiometricDialog()
                            } else {
                                logger.d(TAG, "Set last action to show decrypt biometric dialog")
                                lastAction = { showDecryptBiometricDialog() }
                            }
                        } else {
                            logger.i(TAG, errString)
                            if (needSecret) {
                                if (types.size > 1) showAuthDialog(isDecrypting = true)
                                else secretResult()
                            } else
                                showAuthDialog(isDecrypting = true)
                        }
                    }
                }, { cipher ->
                    logger.order(TAG, "DecryptBiometricDialog preferredType BIOMETRIC_AUTH")
                    typeManager.putPreferredType(AuthType.BIOMETRIC_AUTH)
                    logger.d(TAG, "DecryptBiometricDialog needCheck $needCheck")
                    //if (needCheck) {
                    checkSecret(cipher = cipher)
                    /*} else {
                        refreshWithSecret(cipher = cipher)
                    }*/
                })

                logger.d(TAG, "showDecryptBiometricDialog needCheck=$needCheck")

                dialog.create(context, userInfo)
                val title: String = supporter.resProvider.getString(
                    if (needCheck) supporter.resProvider.provideAccessConfirmStr()
                    else supporter.resProvider.provideAccessStr()
                )
                val negativeText: String = if (needSecret) {
                    val negativeId =
                        if (types.size > 1) supporter.resProvider.provideAlterCancelStr()
                        else supporter.resProvider.provideCancelStr()
                    supporter.resProvider.getString(negativeId)
                } else {
                    val negativeId = supporter.resProvider.provideAlterCancelStr()
                    supporter.resProvider.getString(negativeId)
                }
                dialog.setInfo(title = title, negativeText = negativeText)

                lastAction = {
                    logger.order(TAG, "showDecryptBiometricDialog lastAction DECRYPT_MODE")
                    try {
                        currentBiometricDialog = dialog.authenticate(isDecrypt = true)
                    } catch (e: Exception) {
                        logger.order(TAG, "showDecryptBiometricDialog catch exception")
                        logger.throwable(TAG, e)
                        deleteBiometricType()
                    }
                }
                logger.d(TAG, "showDecryptBiometricDialog stateIsPaused=$stateIsPaused")
                if (!stateIsPaused) {
                    lastAction.invoke()
                    lastAction = {}
                }
            }
        }
    }

    protected open fun showEncryptBiometricDialog(token: Token) {
        logger.order(TAG, "showEncryptBiometricDialog")

        dialogProvider.biom.let { dialog ->
            activity.get()?.let { context ->
                logger.order(TAG, "showDecryptBiometricDialog activity let")
                dialog.onAuthenticationCallback({ isCancelled, errString ->
                    logger.order(TAG, "Encrypt biometric dialog onAuthenticationCallback onError")
                    logger.d(TAG, "onError isCancelled=$isCancelled errString=$errString")
                    context.runOnUiThread {
                        if (isCancelled) {//Fingerprint operation cancelled. on pause
                            logger.d(TAG, "showEncryptBiometricDialog ERROR_CANCELED")
                            lastAction = { showEncryptBiometricDialog(token) }
                        } else {
                            logger.d(
                                TAG,
                                "showEncryptBiometricDialog showAuthDialog(isDecrypting = false,token)"
                            )
                            showAuthDialog(isDecrypting = false, token = token)
                        }
                    }
                }, { cipher ->
                    logger.order(TAG, "EncryptBiometricDialog Success preferredType BIOMETRIC_AUTH")
                    typeManager.putPreferredType(AuthType.BIOMETRIC_AUTH)
                    putSecrets(token = token, cipher = cipher)
                })


                dialog.create(context, userInfo)
                val title: String =
                    supporter.resProvider.getString(supporter.resProvider.provideEncryptTitle())
                val negativeText =
                    supporter.resProvider.getString(supporter.resProvider.provideAlterCancelStr())
                dialog.setInfo(title, negativeText)
                logger.d(TAG, "showEncryptBiometricDialog stateIsPaused=$stateIsPaused")
                lastAction = {
                    logger.order(TAG, "showEncryptBiometricDialog lastAction ENCRYPT_MODE")
                    try {
                        currentBiometricDialog = dialog.authenticate(isDecrypt = false)
                    } catch (e: Exception) {
                        logger.order(TAG, "showEncryptBiometricDialog catch exception")
                        logger.throwable(TAG, e)
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

    /*endregion #################### Biometric END ####################*/

    /*region ##################### Password ########################*/

    protected open fun showPasswordDialog(noSecrets: Boolean = false) {
        logger.order(TAG, "showPasswordDialog")
        dialogProvider.passwordDialog.let { dialog ->
            dialog.createDialog()
            logger.d(TAG, "showPasswordDialog types not empty=${types.isNotEmpty()}")
            logger.d(
                TAG,
                "showPasswordDialog forgotPasswordActionEnable=$forgotPasswordActionEnable"
            )
            dialog.setupParameterBundle(
                cancelBtnRes = supporter.resProvider.provideCancelStr(),
                anotherBtnRes = supporter.resProvider.provideAlterCancelStr(),
                isCancellable = true,
                showAnother = !noSecrets && types.isNotEmpty(),
                forgotPasswordActionEnable = forgotPasswordActionEnable
            )
            dialog.setupResultHandler(providePasswordHandler(noSecrets))

            logger.d(TAG, "showPasswordDialog needCheck=$needCheck")
            if (needCheck)
                dialog.setTitle(supporter.resProvider.provideAccessConfirmStr())
            else
                dialog.setTitle(supporter.resProvider.provideAccessStr())

            showDialog(dialog.getDialog(), "PasswordDialog")
        }
    }

    protected open fun providePasswordHandler(noSecrets: Boolean): PasswordResultHandler =
        object : PasswordResultHandler(noSecrets) {
            override fun onCancel() {
                result()
            }

            override fun onComplete(password: String, recreate: Boolean) {
                logger.order(TAG, "showPasswordDialog onPasswordComplete")
                authenticate(password, recreate, this.noSecrets)
            }

            override fun onForgotClicked() {
                logger.order(
                    TAG,
                    "showPasswordDialog onPasswordForgot forgotPasswordActionEnable=$forgotPasswordActionEnable"
                )
                if (forgotPasswordActionEnable)
                    forgotPasswordAction()
            }

            override fun onAnotherClicked() {
                showAuthDialog(isDecrypting = true)
            }
        }

    /*endregion ##################### Password END ####################*/

    /*################################## Dialogs END ############################################*/

    /*region ################################## Common #################################################*/

    protected open fun showDialog(dialog: IBaseDialog, tag: String) {
        logger.order(TAG, "showDialog $tag stateIsPaused=$stateIsPaused")
        lastAction = {
            logger.order(TAG, "showDialog lastAction invoke")
            //val manager = fragmentManager.get()
            val context = activity.get()
            logger.d(
                TAG,
                "showDialog lastAction fragmentManager != null {manager != null} context != null ${context != null}"
            )
            currentDialog = dialog
            dialog.show(context, tag)
        }
        if (!stateIsPaused) {
            lastAction.invoke()
            lastAction = {}
        }
    }

    protected open fun deleteBiometricType() {
        logger.order(TAG, "deleteBiometricType BIOMETRIC_AUTH")
        executor.deleteSecretTypeUC(AuthType.BIOMETRIC_AUTH) {
            onComplete {
                logger.order(TAG, "deleteBiometricType $it")
                if (it) {
                    types.remove(AuthType.BIOMETRIC_AUTH)
                    if (types.isEmpty()) {
                        val presentType = AuthType.NONE
                        logger.d(
                            TAG,
                            "deleteBiometricType containsSecrets = false presentType=NONE"
                        )
                        showPasswordDialog()
                    } else {
                        val presentType = types.firstOrNull() ?: AuthType.NONE
                        typeManager.putPreferredType(presentType)
                        logger.d(TAG, "deleteBiometricType presentType=$presentType")
                        showAuthDialog(isDecrypting = true)
                    }
                }
            }
            onError {
                logger.errorModel(TAG, it)
                logger.order(TAG, "deleteBiometricType showAuthDialog(isDecrypting = true)")
                showAuthDialog(isDecrypting = true)
            }
        }
    }
    /*endregion #################################### Common END ###########################################*/

    /*region ############################## Settings ###################################################*/
    /**
     * Setting up Activity required for dialogs to work
     *
     * @param activity FragmentActivity
     */
    override fun setActivity(activity: FragmentActivity) {
        logger.order(TAG, "setActivity")
        this.activity = WeakReference(activity)
        activity.lifecycle.addObserver(object : LifecycleObserver {
            //FIX
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun doAfterResume() {
                logger.order(TAG, "activity LifecycleObserver doAfterResume")
                stateIsPaused = false
                lastAction.invoke()
                lastAction = {}
            }

            //FIX
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun doAfterPause() {
                logger.order(TAG, "activity LifecycleObserver doAfterPause")
                stateIsPaused = true
            }
        })
        isBiometricAvailable =
            supporter.util.checkBiometricSupport(activity.applicationContext) { tag: String, msg: String ->
                logger.logCategory(CATEGORY_ERROR, tag, msg)
            }
        logger.d(TAG, "setActivity isBiometricAvailable=$isBiometricAvailable")
    }

    override fun setForgotPasswordAction(enable: Boolean, forgotPasswordAction: () -> Unit) {
        logger.order(TAG, "setForgotPasswordAction enable=$enable")
        this.forgotPasswordActionEnable = enable
        this.forgotPasswordAction = if (enable) forgotPasswordAction else ({})
    }

    override fun setCancellationCallback(callback: IContainerCancellationCallback) {
        logger.order(TAG, "setCancellationCallback")
        containerCancellationCallback = callback
    }

    /*endregion ############################## Settings END ###############################################*/
}