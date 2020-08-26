package by.esas.tools.dialog

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import by.esas.tools.BR
import by.esas.tools.R
import by.esas.tools.databinding.DfPasswordBinding
import by.esas.tools.domain.exception.BaseException
import by.esas.tools.domain.exception.BaseStatusEnum
import by.esas.tools.logger.BaseLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.df_password.df_password_cancel_btn
import kotlinx.android.synthetic.main.df_password.df_password_continue_btn
import kotlinx.android.synthetic.main.df_password.df_password_layout

class GetPasswordDialog : BindingDialogFragment<DfPasswordBinding, BaseException,BaseStatusEnum>() {
    override val TAG: String = GetPasswordDialog::class.java.simpleName

   /* override fun provideLogger(): LoggerImpl {
        return LoggerImpl(TAG)
    }*/

    override fun provideLayoutId(): Int {
        return R.layout.df_password
    }

    override fun provideSwitchableList(): List<TextInputEditText> {
        return listOf(df_password_layout.inputText)
    }

   /* override fun provideValidationList(): List<Validation> {
        return listOf(
            Validation(df_password_layout.inputLayout)
                .add(NotEmptyRule(R.string.validation_password_cannot_be_empty))
                .add(RegexRule(RegexSamples.PASSWORD_REGEX, R.string.validation_password))
        )
    }*/

    override fun provideProgressBar(): View? {
        return null
    }

    fun setPasswordCallback(callback: PasswordCallback) {
        this.callback = callback
    }

    fun setShowRecreateAuth(value: Boolean) {
        showRecreateView = value
    }

    val showForgotPassword = ObservableBoolean(false)
    val showRecreateCheck = ObservableBoolean(false)
    val isChecked = ObservableBoolean(false)
    val password = ObservableField<String>("12345678")

    private var showRecreateView: Boolean = false
    private var callback: PasswordCallback? = null
    private var titleRes: Int = -1
    private var forgotPasswordEnable: Boolean = false
    private var cancelTitleRes: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        //setHeader(view.findViewById(R.id.df_password_title), view.context.getString(titleRes))
        showRecreateCheck.set(showRecreateView)
        showForgotPassword.set(forgotPasswordEnable)
        binding.dfPasswordCancelBtn.setText(cancelTitleRes)
    }

    fun onCancelClick(){
        afterOk = false
        dismiss()
    }

    fun onContinueClick(){
        validatePassword()
    }
    fun onForgotPasswordClick(){
        afterOk = true
        callback?.onPasswordForgot()
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
    }

    override fun disableControls() {
        super.disableControls()
        df_password_continue_btn.isClickable = false
        df_password_cancel_btn.isClickable = false
    }

    override fun enableControls() {
        super.enableControls()
        df_password_continue_btn.isClickable = true
        df_password_cancel_btn.isClickable = true
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

    interface PasswordCallback {
        fun onPasswordComplete(password: String, recreate: Boolean)
        fun onPasswordForgot()
    }

    override fun provideVariableInd(): Int {
        return BR.handler
    }
}