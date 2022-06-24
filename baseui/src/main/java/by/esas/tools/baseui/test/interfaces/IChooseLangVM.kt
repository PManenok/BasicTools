/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.interfaces

import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

interface IChooseLangVM {
    var chooseLanguageBuilder: MaterialAlertDialogBuilder?
    var languageDialog: AlertDialog?

    fun changeLanguage() {
        languageDialog = chooseLanguageBuilder?.show()
    }
}