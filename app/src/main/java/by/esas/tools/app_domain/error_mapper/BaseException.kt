/*
 * Copyright 2024 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.app_domain.error_mapper

class BaseException(message: String) : Exception(message) {

    override fun toString(): String {
        return "BaseException(messageEnum=$message)"
    }

    constructor(enum: Enum<*>) : this(enum.name)
}