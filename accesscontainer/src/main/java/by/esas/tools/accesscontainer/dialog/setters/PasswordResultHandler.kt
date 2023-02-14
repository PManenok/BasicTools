/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

abstract class PasswordResultHandler(protected var noSecrets: Boolean) {

    abstract fun onCancel()
    abstract fun onComplete(password: String, recreate: Boolean = false)
    abstract fun onForgotClicked()
    abstract fun onAnotherClicked()
}