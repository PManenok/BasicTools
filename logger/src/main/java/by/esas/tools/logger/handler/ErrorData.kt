/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger.handler

import android.os.Bundle
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

open class ErrorData<E : Enum<E>, M : BaseErrorModel<E>>(
    val throwable: Throwable? = null,
    val model: M? = null,
    val showType: ShowErrorType,
    actionName: String? = null,
    parameters: Bundle = Bundle()
) : Action(actionName ?: "", parameters)