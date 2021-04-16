/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.entity

class RefreshResult(
    val token: Token? = null,
    val hasAccess: Boolean = true
)