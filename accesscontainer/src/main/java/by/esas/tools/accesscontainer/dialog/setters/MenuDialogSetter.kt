/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

interface MenuDialogSetter : IDialogSetter {
    fun setupParameterBundle(isDecrypting: Boolean, showPassword: Boolean, showPin: Boolean, showBiom: Boolean)
    fun setupResultHandler(handler: MenuResultHandler)
}