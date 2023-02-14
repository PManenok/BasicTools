/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

import androidx.fragment.app.FragmentActivity
import by.esas.tools.accesscontainer.dialog.IBaseDialog

interface IDialogSetter {
    fun createDialog()
    fun getDialog(): IBaseDialog
    fun setTitle(titleRes: Int)
    fun clear(context: FragmentActivity?)
}