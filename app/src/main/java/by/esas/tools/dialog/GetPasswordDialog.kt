package by.esas.tools.dialog

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import by.esas.tools.BR
import by.esas.tools.R
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.DfPasswordBinding
import by.esas.tools.dialog.Config.DIALOG_USER_ACTION

class GetPasswordDialog : BindingDialogFragment<DfPasswordBinding>() {
    override val TAG: String = GetPasswordDialog::class.java.simpleName

    companion object {
        const val USER_ACTION_FORGOT_PASSWORD: String = "USER_ACTION_FORGOT_PASSWORD"
        const val USER_ACTION_COMPLETE_PASSWORD: String = "USER_ACTION_COMPLETE_PASSWORD"
    }

    /* override fun provideLogger(): LoggerImpl {
         return LoggerImpl(TAG)
     }*/

    override fun provideLayoutId(): Int {
        return R.layout.df_password
    }

    override fun provideSwitchableList(): List<EditText> {
        return emptyList()//listOf(df_password_layout.inputText)
    }

    override fun provideValidationList(): List<Checking> {
        return emptyList()/*listOf(
            BaseChecking(df_password_layout.inputLayout)
                .addCheck(NotEmptyCheck("Can't be empty"))
        )*/
    }

    override fun provideProgressBar(): View? {
        return null
    }

    fun setShowRecreateAuth(value: Boolean) {
        showRecreateView = value
    }

    val showForgotPassword = ObservableBoolean(false)
    val showRecreateCheck = ObservableBoolean(false)
    val isChecked = ObservableBoolean(false)
    val password = ObservableField<String>("12345678")

    private var showRecreateView: Boolean = false
    private var titleRes: Int = -1
    private var forgotPasswordEnable: Boolean = false
    private var cancelTitleRes: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        //setHeader(view.findViewById(R.id.df_password_title), view.context.getString(titleRes))
        showRecreateCheck.set(showRecreateView)
        showForgotPassword.set(forgotPasswordEnable)
        if (cancelTitleRes != -1)
            binding.dfPasswordCancelBtn.setText(cancelTitleRes)
    }

    fun onCancelClick() {
        //resultBundle.putString(DIALOG_USER_ACTION, CANCEL_DIALOG)
        dismiss()
    }

    fun onContinueClick() {
        validatePassword()
    }

    fun onForgotPasswordClick() {
        setPasswordForgotResult()
        dismiss()
    }

    private fun validatePassword() {
        /*disableControls()
        Validator.with(App.appContext).setListener(object : Validator.OnValidateListener {
            override fun onValidateFailed() {
                enableControls()
            }

            override fun onValidateSuccess(values: List<String>) {
                callback?.onPasswordComplete(
                    df_password_layout.inputText.text.toString(),
                    isChecked.get()
                )
                afterOk = true
                enableControls()
                dismiss()
            }
        }).validate(*validationList.toTypedArray())*/
        setPasswordCompleteResult(password.get() ?: "", isChecked.get())
        enableControls()
        dismiss()
    }

    override fun disableControls() {
        super.disableControls()
        binding.dfPasswordContinueBtn.isClickable = false
        binding.dfPasswordCancelBtn.isClickable = false
    }

    override fun enableControls() {
        super.enableControls()
        binding.dfPasswordContinueBtn.isClickable = true
        binding.dfPasswordCancelBtn.isClickable = true
    }

    fun setForgotPassword(enable: Boolean) {
        forgotPasswordEnable = enable
    }

    fun setTitle(resId: Int) {
        //titleRes = if (resId == -1) R.string.dialog_password_title else resId
    }

    fun setCancelTitle(resId: Int) {
        //cancelTitleRes = if (resId == -1) R.string.dialog_cancel_btn else resId
    }

    fun setPasswordCompleteResult(password: String, recreate: Boolean) {
        resultBundle.clear()
        resultBundle.putString(DIALOG_USER_ACTION, USER_ACTION_COMPLETE_PASSWORD)
        resultBundle.putString("PASSWORD", password)
        resultBundle.putBoolean("RECREATE", recreate)
    }

    fun setPasswordForgotResult() {
        resultBundle.clear()
        resultBundle.putString(DIALOG_USER_ACTION, USER_ACTION_FORGOT_PASSWORD)
    }

    override fun provideVariableId(): Int {
        return BR.handler
    }
}