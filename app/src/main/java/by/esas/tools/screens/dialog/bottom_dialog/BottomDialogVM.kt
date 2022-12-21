package by.esas.tools.screens.dialog.bottom_dialog

import by.esas.tools.base.AppVM
import by.esas.tools.dialog.CustomBottomDialog
import javax.inject.Inject

class BottomDialogVM @Inject constructor(): AppVM() {

    fun showBottomDialog(requestKey: String) {
        showDialog(CustomBottomDialog().apply {
            setRequestKey(requestKey)
            isCancelable = false
        })
    }
}