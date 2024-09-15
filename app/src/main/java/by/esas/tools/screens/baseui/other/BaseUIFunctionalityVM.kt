package by.esas.tools.screens.baseui.other

import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.base.AppVM
import by.esas.tools.dialog_message.MessageDialog
import javax.inject.Inject

class BaseUIFunctionalityVM @Inject constructor() : AppVM() {

    fun showMessageDialog() {
        val dialog = by.esas.tools.dialog_message.MessageDialog()
        dialog.setMessage(App.appContext.resources.getString(R.string.baseui_functionality_test_message))
        dialog.setTitle(App.appContext.resources.getString(R.string.baseui_functionality_test_dialog))
        showDialog(dialog)
    }
}