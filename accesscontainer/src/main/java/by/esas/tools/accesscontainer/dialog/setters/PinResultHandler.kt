/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

import by.esas.tools.accesscontainer.entity.Token

abstract class PinResultHandler(protected val token: Token?) {
    abstract fun onCancel()
    abstract fun onAnotherClicked()
    abstract fun onComplete(pin: String)
}