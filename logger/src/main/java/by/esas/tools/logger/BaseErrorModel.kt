/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

open class BaseErrorModel<E : Enum<E>>(
    override val code: Int,
    override val statusEnum: E
) : IErrorModel<E> {
    override fun toString(): String {
        return "ErrorModel(code=$code, statusEnum=${statusEnum.name})"
    }
}
