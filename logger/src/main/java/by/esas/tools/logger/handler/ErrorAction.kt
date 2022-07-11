/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger.handler

import android.os.Bundle
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

open class ErrorAction<M : BaseErrorModel> protected constructor(val model: M?, parameters: Bundle? = null) :
    Action(ACTION_ERROR, parameters) {

    companion object {
        const val ACTION_ERROR: String = "ACTION_ERROR"
        const val ACTION_PARAM_SUB_NAME: String = "ACTION_PARAM_SUB_NAME"
        const val ACTION_PARAM_SHOW_TYPE: String = "ACTION_PARAM_SHOW_TYPE"

        fun <M : BaseErrorModel> create(
            model: M,
            showType: String,
            actionName: String?,
            parameters: Bundle?
        ): ErrorAction<M> {
            val params: Bundle = parameters ?: Bundle()
            if (!actionName.isNullOrBlank()) {
                //when action is not null add it to bundle
                params.putString(ACTION_PARAM_SUB_NAME, actionName)
            }
            params.putAll(model.toBundle())
            params.putString(ACTION_PARAM_SHOW_TYPE, showType)
            return ErrorAction(model = model, parameters = params)
        }

        fun <M : BaseErrorModel> create(
            model: M,
            showType: String,
            actionName: String
        ): ErrorAction<M> {
            return create(model, showType, actionName, null)
        }

        fun <M : BaseErrorModel> create(
            model: M,
            showType: String,
            parameters: Bundle
        ): ErrorAction<M> {
            return create(model, showType, null, parameters)
        }
    }

    fun getShowType(): String {
        return parameters?.getString(ACTION_PARAM_SHOW_TYPE) ?: ""
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

    override fun toString(): String {
        return "ErrorAction(model=$model) ${super.toString()}"
    }
}