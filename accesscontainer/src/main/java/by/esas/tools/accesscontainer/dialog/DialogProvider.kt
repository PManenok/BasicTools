package by.esas.tools.accesscontainer.dialog

import by.esas.tools.accesscontainer.dialog.setters.MenuDialogSetter
import by.esas.tools.accesscontainer.dialog.setters.SetBiometricDialog
import by.esas.tools.accesscontainer.dialog.setters.SetPasswordDialog
import by.esas.tools.accesscontainer.dialog.setters.SetPinDialog

abstract class DialogProvider {
    abstract val passwordDialog: SetPasswordDialog
    abstract val menuDialog: MenuDialogSetter
    abstract val pinDialog: SetPinDialog
    abstract val biom: SetBiometricDialog

    fun clear() {
        passwordDialog.clear()
        menuDialog.clear()
        pinDialog.clear()
        biom.clear()
    }
}