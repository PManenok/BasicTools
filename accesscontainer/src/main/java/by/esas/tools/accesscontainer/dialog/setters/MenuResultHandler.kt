/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

import by.esas.tools.accesscontainer.entity.Token

abstract class MenuResultHandler(
    protected val decrypting: Boolean,
    protected val accessToken: Token?
) {

    abstract fun onCancel()
    abstract fun onPinClick()
    abstract fun onPasswordClick()
    abstract fun onBiomClick()
}