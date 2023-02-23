/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.error

import by.esas.tools.logger.BaseErrorModel

interface IErrorMapper<M : BaseErrorModel> {

    fun mapError(e: Exception): M
    fun createErrorModel(code: Int, enumName: String): M

    operator fun invoke(e: Exception): M {
        return mapError(e)
    }

    operator fun invoke(code: Int, enumName: String): M {
        return createErrorModel(code, enumName)
    }
}