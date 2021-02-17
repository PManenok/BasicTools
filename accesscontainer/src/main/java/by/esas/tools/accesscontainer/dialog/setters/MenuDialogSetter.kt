package by.esas.tools.accesscontainer.dialog.setters

interface MenuDialogSetter :
    IDialogSetter {
    fun isDecrypting(decrypting: Boolean)
    fun setCallBacks(pin: () -> Unit?, biom: () -> Unit?, password: () -> Unit?)
    fun showPassword(value: Boolean)
    fun setPinPresent(present: Boolean)
    fun setBiometricPresent(present: Boolean)
}