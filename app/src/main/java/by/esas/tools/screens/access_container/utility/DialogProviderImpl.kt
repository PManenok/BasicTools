package by.esas.tools.screens.access_container.utility

import android.view.View
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentResultListener
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.accesscontainer.dialog.BiometricUserInfo
import by.esas.tools.accesscontainer.dialog.DialogProvider
import by.esas.tools.accesscontainer.dialog.IBaseDialog
import by.esas.tools.accesscontainer.dialog.IBiometric
import by.esas.tools.accesscontainer.dialog.setters.MenuDialogSetter
import by.esas.tools.accesscontainer.dialog.setters.MenuResultHandler
import by.esas.tools.accesscontainer.dialog.setters.PasswordResultHandler
import by.esas.tools.accesscontainer.dialog.setters.PinResultHandler
import by.esas.tools.accesscontainer.dialog.setters.SetBiometricDialog
import by.esas.tools.accesscontainer.dialog.setters.SetPasswordDialog
import by.esas.tools.accesscontainer.dialog.setters.SetPinDialog
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.biometric_decryption.Biometric
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.dialog.Config
import by.esas.tools.dialog.Config.CANCEL_DIALOG
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.dialog.MessageDialog.Companion.ITEM_CODE
import by.esas.tools.dialog.MessageDialog.Companion.USER_ACTION_ITEM_PICKED
import by.esas.tools.logger.ILogger
import by.esas.tools.screens.access_container.dialog.PasswordDialog
import by.esas.tools.screens.access_container.dialog.PinDialog
import by.esas.tools.utils.logger.ErrorModel
import javax.crypto.Cipher

class DialogProviderImpl(val logger: ILogger<ErrorModel>) : DialogProvider() {

    override val passwordDialog: SetPasswordDialog = providePasswordDialog()
    override val menuDialog: MenuDialogSetter = provideMenuDialog()
    override val pinDialog: SetPinDialog = providePinDialog()
    override val biom: SetBiometricDialog = provideBiomDialog()

    private fun show(
        context: FragmentActivity?,
        tag: String,
        instance: BaseDialogFragment?,
        resultListener: FragmentResultListener
    ) {
        logger.logOrder("IBaseDialog $tag show instance != null ${instance != null}")
        if (context != null) {
            instance?.setRequestKey(tag)

            context.supportFragmentManager.clearFragmentResultListener(tag)
            context.supportFragmentManager.setFragmentResultListener(tag, context, resultListener)

            try {
                instance?.show(context.supportFragmentManager, tag)
            } catch (e: java.lang.IllegalStateException) {
                context.supportFragmentManager.clearFragmentResultListener(tag)
                logger.logError("IBaseDialog $tag IllegalStateException while dialog show")
            }
        }
    }

    private fun provideMenuDialog(): MenuDialogSetter {
        var dialog: MessageDialog? = null

        return object : MenuDialogSetter {
            val list = mutableListOf<Pair<String, AuthType>>()
            var menuHandler: MenuResultHandler? = null

            override fun createDialog() {
                logger.logOrder("MenuDialogSetter createDialog dialog!=null = ${dialog != null}")
                dialog?.dismiss()
                dialog = null
                dialog = MessageDialog(false)
                dialog?.isCancelable = false
            }

            override fun setupParameterBundle(
                isDecrypting: Boolean,
                showPassword: Boolean,
                showPin: Boolean,
                showBiom: Boolean
            ) {
                logger.logOrder("MenuDialogSetter setupParameterBundle isDecrypting=$isDecrypting, showPassword=$showPassword, showPin=$showPin, showBiom=$showBiom")
                list.clear()
                if (isDecrypting && showPassword)
                    list.add(Pair(App.appContext.getString(R.string.label_password), AuthType.NONE))
                if (showPin)
                    list.add(Pair(App.appContext.getString(R.string.label_pin), AuthType.PIN_AUTH))
                if (showBiom)
                    list.add(Pair(App.appContext.getString(R.string.label_biom), AuthType.BIOMETRIC_AUTH))
                dialog?.setItems(
                    list = list.map { MessageDialog.ItemInfo(it.second.name, it.first) },
                    alignment = View.TEXT_ALIGNMENT_CENTER
                )
            }

            override fun setupResultHandler(handler: MenuResultHandler) {
                logger.logOrder("MenuDialogSetter setupResultHandler")
                this.menuHandler = handler
            }

            override fun setTitle(titleRes: Int) {
                logger.logOrder("MenuDialogSetter setTitle dialog!=null = ${dialog != null}")
                dialog?.setTitle(titleRes)
            }

            override fun getDialog(): IBaseDialog {
                logger.logOrder("MenuDialogSetter getDialog dialog != null = ${dialog != null}")
                return object : IBaseDialog {
                    var dialogInst: BaseDialogFragment? = dialog

                    override fun dismiss() {
                        logger.logOrder("IBaseDialog MessageDialog dismiss dialogInst != null = ${dialogInst != null}")
                        try {
                            dialogInst?.dismiss()
                            dialogInst = null
                        } catch (e: IllegalStateException) {
                            logger.logError(e)
                        }
                    }

                    override fun show(context: FragmentActivity?, tag: String) {
                        val listener = provideResultListener(tag)
                        show(context, tag, dialogInst, listener)
                    }

                    override fun provideResultListener(tag: String): FragmentResultListener {
                        return FragmentResultListener { requestKey, result ->
                            if (requestKey == tag) {
                                val userAction = result.getString(Config.DIALOG_USER_ACTION)
                                logger.logOrder("IBaseDialog $tag requestKey $requestKey userAction $userAction")
                                if (userAction == CANCEL_DIALOG) {
                                    menuHandler?.onCancel()
                                } else if (userAction == USER_ACTION_ITEM_PICKED) {
                                    val itemCode = result.getString(ITEM_CODE) ?: ""
                                    val type: AuthType = try {
                                        AuthType.valueOf(itemCode)
                                    } catch (e: IllegalArgumentException) {
                                        AuthType.NONE
                                    }
                                    when (type) {
                                        AuthType.BIOMETRIC_AUTH -> menuHandler?.onBiomClick()
                                        AuthType.PIN_AUTH -> menuHandler?.onPinClick()
                                        AuthType.NONE -> menuHandler?.onPasswordClick()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun clear(context: FragmentActivity?) {
                logger.logOrder("MenuDialogSetter clear dialog!=null = ${dialog != null}")
                dialog?.getRequestKey()?.let { context?.supportFragmentManager?.clearFragmentResultListener(it) }
                dialog?.dismiss()
                dialog = null
            }
        }
    }

    private fun providePasswordDialog(): SetPasswordDialog {
        return object : SetPasswordDialog {
            var dialog: PasswordDialog? = null
            var resultHandler: PasswordResultHandler? = null

            override fun setupParameterBundle(
                cancelBtnRes: Int,
                anotherBtnRes: Int,
                isCancellable: Boolean,
                showAnother: Boolean,
                forgotPasswordActionEnable: Boolean
            ) {
                logger.logOrder("GetPasswordDialog setupParameterBundle")
                dialog?.isCancelable = isCancellable
                dialog?.showAnotherMethod(showAnother)
                dialog?.setForgotPassword(forgotPasswordActionEnable)
            }

            override fun setupResultHandler(handler: PasswordResultHandler) {
                logger.logOrder("GetPasswordDialog setupResultHandler")
                this.resultHandler = handler
            }

            override fun setTitle(titleRes: Int) {
                logger.logOrder("GetPasswordDialog setTitle dialog!=null = ${dialog != null}")
                dialog?.setTitle(titleRes)
            }

            override fun createDialog() {
                logger.logOrder("GetPasswordDialog createDialog dialog!=null = ${dialog != null}")
                dialog?.dismiss()
                dialog = null
                dialog = PasswordDialog()
            }

            override fun getDialog(): IBaseDialog {
                logger.logOrder("GetPasswordDialog getDialog dialog != null = ${dialog != null}")
                return object : IBaseDialog {
                    var dialogInst: BaseDialogFragment? = dialog
                    override fun dismiss() {
                        logger.logOrder("IBaseDialog PasswordDialog dismiss dialogInst != null = ${dialogInst != null}")
                        try {
                            dialogInst?.dismiss()
                            dialogInst = null
                        } catch (e: IllegalStateException) {
                            logger.logError(e)
                        }
                    }

                    override fun show(context: FragmentActivity?, tag: String) {
                        val listener = provideResultListener(tag)
                        show(context, tag, dialogInst, listener)
                    }

                    override fun provideResultListener(tag: String): FragmentResultListener {
                        return FragmentResultListener { requestKey, result ->
                            if (requestKey == tag) {
                                val userAction = result.getString(Config.DIALOG_USER_ACTION)
                                logger.logOrder("IBaseDialog $tag requestKey $requestKey userAction $userAction")
                                when (userAction) {
                                    CANCEL_DIALOG -> {
                                        resultHandler?.onCancel()
                                    }
                                    PasswordDialog.USER_ACTION_ANOTHER_CLICKED -> {
                                        resultHandler?.onAnotherClicked()
                                    }
                                    PasswordDialog.USER_ACTION_PASSWORD_COMPLETED -> {
                                        val password = result.getString(PasswordDialog.RESULT_PASSWORD)
                                        resultHandler?.onComplete(password ?: "")
                                    }
                                    PasswordDialog.USER_ACTION_FORGOT_CLICKED -> {
                                        resultHandler?.onForgotClicked()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun clear(context: FragmentActivity?) {
                logger.logOrder("GetPasswordDialog clear dialog!=null = ${dialog != null}")
                dialog?.getRequestKey()?.let { context?.supportFragmentManager?.clearFragmentResultListener(it) }
                dialog?.dismiss()
                dialog = null
            }
        }
    }

    private fun providePinDialog(): SetPinDialog {
        return object : SetPinDialog {
            var dialog: PinDialog? = null
            var resultHandler: PinResultHandler? = null

            override fun createDialog() {
                logger.logOrder("SetPinDialog createDialog dialog!=null = ${dialog != null}")
                dialog?.dismiss()
                dialog = null
                dialog = PinDialog()
            }

            override fun setTitle(titleRes: Int) {
                logger.logOrder("SetPinDialog setTitle dialog!=null = ${dialog != null}")
                dialog?.setTitle(titleRes)
            }

            override fun setupParameterBundle(
                cancelBtnRes: Int,
                anotherBtnRes: Int,
                cancellable: Boolean,
                hasAnother: Boolean
            ) {
                logger.logOrder("SetPinDialog dialog!=null = ${dialog != null}")
                logger.logInfo("SetPinDialog setCancellable $cancellable")
                dialog?.isCancelable = cancellable
                logger.logInfo("SetPinDialog hasAnother $hasAnother")
                dialog?.setHasAnother(hasAnother)
            }

            override fun setupResultHandler(handler: PinResultHandler) {
                this.resultHandler = handler
            }

            override fun getDialog(): IBaseDialog {
                logger.logOrder("SetPinDialog getDialog dialog != null = ${dialog != null}")
                return object : IBaseDialog {
                    var dialogInst: BaseDialogFragment? = dialog
                    override fun dismiss() {
                        logger.logOrder("IBaseDialog PinDialog dismiss dialogInst != null = ${dialogInst != null}")
                        try {
                            dialogInst?.dismiss()
                            dialogInst = null
                        } catch (e: IllegalStateException) {
                            logger.logError(e)
                        }
                    }

                    override fun show(context: FragmentActivity?, tag: String) {
                        val listener = provideResultListener(tag)
                        show(context, tag, dialogInst, listener)
                    }

                    override fun provideResultListener(tag: String): FragmentResultListener {
                        return FragmentResultListener { requestKey, result ->
                            if (requestKey == tag) {
                                val userAction = result.getString(Config.DIALOG_USER_ACTION)
                                logger.logOrder("IBaseDialog $tag requestKey $requestKey userAction $userAction")
                                when (userAction) {
                                    CANCEL_DIALOG -> {
                                        resultHandler?.onCancel()
                                    }
                                    PinDialog.USER_ACTION_ANOTHER_CLICKED -> {
                                        resultHandler?.onAnotherClicked()
                                    }
                                    PinDialog.USER_ACTION_PIN_COMPLETED -> {
                                        val pin = result.getString(PinDialog.RESULT_PIN)
                                        resultHandler?.onComplete(pin ?: "")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun clear(context: FragmentActivity?) {
                logger.logOrder("GetPasswordDialog clear dialog!=null = ${dialog != null}")
                dialog?.getRequestKey()?.let { context?.supportFragmentManager?.clearFragmentResultListener(it) }
                dialog?.dismiss()
                dialog = null
            }
        }
    }

    private fun provideBiomDialog(): SetBiometricDialog {
        return object : SetBiometricDialog {
            var biometricCallback: BiometricPrompt.AuthenticationCallback? = null
            var builder: Biometric? = null
            var prompt: BiometricPrompt.PromptInfo? = null
            var dialog: BiometricPrompt? = null

            override fun onAuthenticationCallback(onError: (Boolean, String) -> Unit, onSuccess: (Cipher?) -> Unit) {
                logger.logOrder("SetBiometricDialog onAuthenticationCallback")
                biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        logger.logOrder("SetBiometricDialog authentication Error")
                        onError(errorCode == BiometricPrompt.ERROR_CANCELED, errString.toString())
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        logger.logOrder("SetBiometricDialog authentication Succeeded")
                        onSuccess(result.cryptoObject?.cipher)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        logger.logOrder("SetBiometricDialog authentication Failed")
                    }
                }
                logger.logOrder("SetBiometricDialog biometricCallback!=null = ${biometricCallback != null}")
            }

            override fun create(activity: FragmentActivity, userInfo: BiometricUserInfo) {
                logger.logOrder("SetBiometricDialog create")
                val info = object : Biometric.BiometricUserInfo {
                    override fun getCurrentUser(): String {
                        return userInfo.getCurrentUser()
                    }

                    override fun setCurrentUserIv(iv: ByteArray) {
                        userInfo.setCurrentUserIv(iv)
                    }

                    override fun getCurrentUserIv(): ByteArray {
                        return userInfo.getCurrentUserIv()
                    }
                }
                builder = biometricCallback?.let { callback ->
                    logger.logOrder("SetBiometricDialog builder update")
                    Biometric(activity, callback, info)
                }
                logger.logOrder("SetBiometricDialog builder!=null = ${builder != null}")
            }

            override fun setInfo(title: String, negativeText: String) {
                logger.logOrder("SetBiometricDialog setInfo")
                prompt = builder?.createInfo(title, negativeText)
                logger.logOrder("SetBiometricDialog prompt!=null = ${prompt != null}")
            }

            override fun authenticate(isDecrypt: Boolean): IBiometric? {
                logger.logOrder("SetBiometricDialog authenticate isDecrypt = $isDecrypt")
                prompt?.let { info ->
                    logger.logOrder("SetBiometricDialog authenticate prompt let")
                    dialog = builder?.authenticate(
                        if (isDecrypt) Biometric.DialogMode.DECRYPT_MODE else Biometric.DialogMode.ENCRYPT_MODE,
                        info
                    )
                }
                logger.logOrder("SetBiometricDialog authenticate return IBiometric")
                return object : IBiometric {
                    private var dialogInst: BiometricPrompt? = dialog
                    override fun cancelAuthentication() {
                        logger.logOrder("IBiometric cancelAuthentication dialogInst!=null ${dialogInst != null}")
                        dialogInst?.cancelAuthentication()
                        dialogInst = null
                    }
                }
            }

            override fun clear() {
                logger.logOrder("SetBiometricDialog clear")
                biometricCallback = null
                builder = null
                prompt = null
                dialog?.cancelAuthentication()
                dialog = null
            }
        }
    }
}