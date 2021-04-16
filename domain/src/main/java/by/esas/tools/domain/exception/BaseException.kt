/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.domain.exception

class BaseException(message: String) : Exception(message) {
    override fun toString(): String {
        return "BaseException(messageEnum=$message)"
    }

    constructor(enum: Enum<*>) : this(enum.name)
}