/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog

interface IBiometric {
    fun cancelAuthentication()
    //fun authenticate(isDecrypt: Boolean, title: String, negativeText: String): IBiometric?
}