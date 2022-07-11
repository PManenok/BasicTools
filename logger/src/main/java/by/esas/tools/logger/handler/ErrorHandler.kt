/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger.handler

import by.esas.tools.logger.BaseErrorModel

abstract class ErrorHandler<M : BaseErrorModel> {
    abstract fun getErrorMessage(error: M): String
    abstract fun getErrorMessage(e: Throwable): String
    abstract fun mapError(e: Throwable): M
}