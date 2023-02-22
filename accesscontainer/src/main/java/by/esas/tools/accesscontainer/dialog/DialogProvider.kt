/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog

import androidx.fragment.app.FragmentActivity
import by.esas.tools.accesscontainer.dialog.setters.MenuDialogSetter
import by.esas.tools.accesscontainer.dialog.setters.SetBiometricDialog
import by.esas.tools.accesscontainer.dialog.setters.SetPasswordDialog
import by.esas.tools.accesscontainer.dialog.setters.SetPinDialog

abstract class DialogProvider {
    abstract val passwordDialog: SetPasswordDialog
    abstract val menuDialog: MenuDialogSetter
    abstract val pinDialog: SetPinDialog
    abstract val biom: SetBiometricDialog

    fun clear(context: FragmentActivity?) {
        passwordDialog.clear(context)
        menuDialog.clear(context)
        pinDialog.clear(context)
        biom.clear()
    }
}