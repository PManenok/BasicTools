/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.support

import by.esas.tools.accesscontainer.entity.AuthType

interface ITypeManager {
    fun getPreferredType(): AuthType
    fun putPreferredType(type: AuthType)
}