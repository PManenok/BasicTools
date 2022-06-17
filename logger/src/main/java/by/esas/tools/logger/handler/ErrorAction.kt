/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger.handler

import android.os.Bundle
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

open class ErrorAction<E : Enum<E>, M : BaseErrorModel<E>>(
    val throwable: Throwable? = null,
    val model: M? = null,
    val showType: String,
    parameters: Bundle? = null
) : Action(ACTION_ERROR, parameters) {

    companion object {
        const val ACTION_ERROR: String = "ACTION_ERROR"
        const val ACTION_PARAM_SUB_NAME: String = "ACTION_PARAM_SUB_NAME"

        fun <E : Enum<E>, M : BaseErrorModel<E>> create(
            throwable: Throwable?,
            model: M?,
            showType: String,
            actionName: String?,
            parameters: Bundle?
        ): ErrorAction<E, M> {
            val params: Bundle = parameters ?: Bundle()
            if (!actionName.isNullOrBlank()) {
                //when action is not null add it to bundle
                params.putString(ACTION_PARAM_SUB_NAME, actionName)
            }
            return ErrorAction(
                throwable = throwable,
                model = model,
                showType = showType,
                parameters = params
            )
        }

        fun <E : Enum<E>, M : BaseErrorModel<E>> create(
            model: M,
            showType: String,
            actionName: String,
            parameters: Bundle
        ): ErrorAction<E, M> {
            return create(null, model, showType, actionName, parameters)
        }

        fun <E : Enum<E>, M : BaseErrorModel<E>> create(
            model: M,
            showType: String,
            actionName: String
        ): ErrorAction<E, M> {
            return create(null, model, showType, actionName, null)
        }

        fun <E : Enum<E>, M : BaseErrorModel<E>> create(
            model: M,
            showType: String
        ): ErrorAction<E, M> {
            return create(null, model, showType, null, null)
        }

        fun <E : Enum<E>, M : BaseErrorModel<E>> create(
            throwable: Throwable,
            showType: String,
            actionName: String,
            parameters: Bundle
        ): ErrorAction<E, M> {
            return create(throwable, null, showType, actionName, parameters)
        }

        fun <E : Enum<E>, M : BaseErrorModel<E>> create(
            throwable: Throwable,
            showType: String,
            actionName: String
        ): ErrorAction<E, M> {
            return create(throwable, null, showType, actionName, null)
        }

        fun <E : Enum<E>, M : BaseErrorModel<E>> create(
            throwable: Throwable,
            showType: String
        ): ErrorAction<E, M> {
            return create(throwable, null, showType, null, null)
        }
    }

    fun getSubActionName(): String? {
        return parameters?.getString(ACTION_PARAM_SUB_NAME)
    }

    fun getSubAction(): Action? {
        return if (parameters?.containsKey(ACTION_PARAM_SUB_NAME) == true) {
            val name: String = parameters.getString(ACTION_PARAM_SUB_NAME) ?: ACTION_NOT_SET
            parameters.remove(ACTION_PARAM_SUB_NAME)
            Action(name, parameters)
        } else {
            null
        }
    }
}