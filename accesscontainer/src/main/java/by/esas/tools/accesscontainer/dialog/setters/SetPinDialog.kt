/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

interface SetPinDialog : IDialogSetter {

    fun setupParameterBundle(
        cancelBtnRes: Int,
        anotherBtnRes: Int,
        cancellable: Boolean,
        hasAnother: Boolean
    )

    fun setupResultHandler(handler: PinResultHandler)
}