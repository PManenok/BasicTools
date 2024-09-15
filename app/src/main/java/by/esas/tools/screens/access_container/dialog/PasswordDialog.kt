package by.esas.tools.screens.access_container.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import by.esas.tools.BR
import by.esas.tools.R
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.DfPasswordBinding
import by.esas.tools.dialog.BindingDialogFragment
import by.esas.tools.dialog_core.Config
import by.esas.tools.utils.checking.AppChecker

class PasswordDialog : BindingDialogFragment<DfPasswordBinding>() {

    companion object {

        const val USER_ACTION_PASSWORD_COMPLETED: String = "USER_ACTION_PASSWORD_COMPLETED"
        const val RESULT_PASSWORD: String = "RESULT_PASSWORD"
        const val USER_ACTION_ANOTHER_CLICKED: String = "USER_ACTION_ANOTHER_CLICKED"
        const val USER_ACTION_FORGOT_CLICKED: String = "USER_ACTION_FORGOT_CLICKED"
    }

    override fun provideLayoutId(): Int {
        return R.layout.df_password
    }

    override fun provideSwitchableList(): List<View?> {
        return emptyList()
    }

    override fun provideValidationList(): List<Checking> {
        return emptyList()
    }

    override fun provideVariableId(): Int {
        return BR.handler
    }

    val showForgotPassword = ObservableBoolean(false)
    val showAnotherBtn = ObservableBoolean(false)
    val btnEnabled = ObservableBoolean(false)
    val password = ObservableField<String>("")

    private var titleRes: Int = R.string.label_password
    private var showForgot: Boolean = false
    private var showAnother: Boolean = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("btnEnabled", btnEnabled.get())
        outState.putString("password", password.get())
        outState.putBoolean("showForgot", showForgot)
        outState.putBoolean("showAnother", showAnother)
        outState.putInt("titleRes", titleRes)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            btnEnabled.set(savedInstanceState.getBoolean("btnEnabled", true))
            showForgot = (savedInstanceState.getBoolean("showForgot", false))
            showAnother = (savedInstanceState.getBoolean("showAnother", false))
            password.set(savedInstanceState.getString("password", "") ?: "")
            titleRes = (savedInstanceState.getInt("titleRes", R.string.label_password))
            updateScreen()
        }
    }

    override fun styleSettings() {
        setStyle(STYLE_NO_FRAME, 0)
    }

    override fun layoutSettings() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateScreen()
        enableControls()
    }

    fun updateScreen() {
        binding.dfPasswordTopBarTitle.setText(titleRes)

        showAnotherBtn.set(showAnother)
        showForgotPassword.set(showForgot)
    }

    fun onBackClick() {
        disableControls()
        resultBundle.clear()
        resultBundle.putString(Config.DIALOG_USER_ACTION, Config.CANCEL_DIALOG)
        dismiss()
    }

    fun onAnotherClick() {
        disableControls()
        resultBundle.clear()
        resultBundle.putString(Config.DIALOG_USER_ACTION, USER_ACTION_ANOTHER_CLICKED)
        dismiss()
        enableControls()
    }

    fun onPasswordClick() {
        disableControls()
        validatePassword()
    }

    fun onForgotClick() {
        disableControls()
        resultBundle.clear()
        resultBundle.putString(Config.DIALOG_USER_ACTION, USER_ACTION_FORGOT_CLICKED)
        dismiss()
        enableControls()
    }

    private fun validatePassword() {
        AppChecker().setListener(object : Checker.CheckListener {
            override fun onFailed() {
                enableControls()
            }

            override fun onSuccess() {
                resultBundle.clear()
                resultBundle.putString(Config.DIALOG_USER_ACTION, USER_ACTION_PASSWORD_COMPLETED)
                resultBundle.putString(RESULT_PASSWORD, password.get() ?: "")
                dismiss()
                enableControls()
            }
        }).validate(provideValidationList())
    }

    override fun disableControls() {
        super.disableControls()
        btnEnabled.set(false)
    }

    override fun enableControls() {
        super.enableControls()
        btnEnabled.set(true)
    }

    fun showAnotherMethod(show: Boolean) {
        showAnother = show
    }

    fun setForgotPassword(enable: Boolean) {
        showForgot = enable
    }

    fun setTitle(resId: Int) {
        titleRes = if (resId == -1) R.string.label_password else resId
    }
}