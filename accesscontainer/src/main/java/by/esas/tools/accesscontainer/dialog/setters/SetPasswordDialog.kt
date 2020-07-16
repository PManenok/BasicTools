package by.esas.tools.accesscontainer.dialog.setters

interface SetPasswordDialog :
    IDialogSetter {
    fun setCancelTitle(cancelBtnRes: Int)
    fun setShowRecreateAuth(recreate: Boolean)
    fun setForgotPassword(forgotPasswordActionEnable: Boolean)

    fun setCallbacks(complete: (String, Boolean) -> Unit, forgot: () -> Unit)
}