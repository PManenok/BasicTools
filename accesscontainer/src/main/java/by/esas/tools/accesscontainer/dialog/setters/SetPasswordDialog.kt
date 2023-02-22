/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

interface SetPasswordDialog : IDialogSetter {
    fun setupParameterBundle(
        cancelBtnRes: Int,
        anotherBtnRes: Int,
        isCancellable: Boolean,
        showAnother: Boolean,
        forgotPasswordActionEnable: Boolean
    )

    fun setupResultHandler(handler: PasswordResultHandler)
}