package by.esas.tools.accesscontainer.dialog.setters

import by.esas.tools.accesscontainer.dialog.IBaseDialog

interface IDialogSetter {
    fun createDialog()
    fun getDialog(): IBaseDialog
    fun setStateActions(error: (Exception) -> Unit, dismiss: (Boolean) -> Unit)

    fun setTitle(titleRes: Int)

    fun clear()
}