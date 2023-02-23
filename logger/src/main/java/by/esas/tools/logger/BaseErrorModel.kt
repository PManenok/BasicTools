/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

import android.os.Bundle

open class BaseErrorModel(
    val code: Int,
    val statusEnum: String
) {

    companion object {

        const val ERROR_MODEL_CODE: String = "ERROR_MODEL_CODE"
        const val ERROR_MODEL_STATUS: String = "ERROR_MODEL_STATUS"
        const val ERROR_MODEL_STATUS_NOT_SET: String = "NOT_SET"

        fun fromBundle(bundle: Bundle?): BaseErrorModel? {
            return if (bundle != null) {
                val tempCode: Int = bundle.getInt(ERROR_MODEL_CODE)
                val tempStatus: String = bundle.getString(ERROR_MODEL_STATUS, ERROR_MODEL_STATUS_NOT_SET)
                BaseErrorModel(tempCode, tempStatus)
            } else {
                null
            }
        }
    }

    override fun toString(): String {
        return "ErrorModel(code=$code, statusEnum=${statusEnum})"
    }

    open fun toBundle(): Bundle {
        return Bundle().apply {
            putInt(ERROR_MODEL_CODE, code)
            putString(ERROR_MODEL_STATUS, statusEnum)
        }
    }
}
