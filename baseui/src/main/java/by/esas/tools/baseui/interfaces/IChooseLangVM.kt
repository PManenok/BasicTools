package by.esas.tools.baseui.interfaces

import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

interface IChooseLangVM {
    var chooseLanguageBuilder: MaterialAlertDialogBuilder?
    var languageDialog: AlertDialog?

    fun changeLanguage() {
        languageDialog = chooseLanguageBuilder?.show()
    }
}