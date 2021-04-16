/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

interface SetPinDialog :
    IDialogSetter {
    fun setCallbacks(complete: (String) -> Unit, cancel: () -> Unit)
    fun setCancelTitle(cancelRes: Int)
    fun setCancellable(value: Boolean)
    fun setHasAnother(value: Boolean)
}