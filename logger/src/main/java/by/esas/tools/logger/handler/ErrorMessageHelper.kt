/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger.handler

import by.esas.tools.logger.BaseErrorModel

/**
 * Preferable to create object to implement this interface and to use it everywhere.
 * But also possible to use as class implementation
 */
interface ErrorMessageHelper<M : BaseErrorModel> {

    fun getErrorMessage(error: M): String
}