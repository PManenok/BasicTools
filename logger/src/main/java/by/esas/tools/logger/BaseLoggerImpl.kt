/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Base logger implementation
 */
class BaseLoggerImpl<E : Enum<E>, M : BaseErrorModel<E>>(tag: String? = null, private var context: Context? = null) :
    ILogger<E, M> {
    override var currentTag: String = BaseLoggerImpl::class.java.simpleName

    init {
        if (!tag.isNullOrBlank())
            this.currentTag = tag
    }

    fun setContext(context: Context? = null) {
        this.context = context
    }

    override fun log(tag: String, msg: String, level: Int) {
        when (level) {
            Log.ERROR -> {
                Log.e(tag, msg)
            }
            Log.WARN -> {
                Log.w(tag, msg)
            }
            Log.DEBUG -> {
                Log.d(tag, msg)
            }
            else -> {
                Log.i(tag, msg)
            }
        }
    }

    override fun logCategory(category: String, tag: String, msg: String) {
        when (category) {
            ILogger.CATEGORY_ERROR -> {
                log(currentTag, msg, Log.ERROR)
            }
            ILogger.CATEGORY_IMPORTANT -> {
                log(currentTag, msg, Log.WARN)
            }
            ILogger.CATEGORY_INFO, ILogger.CATEGORY_ORDER -> {
                log(currentTag, msg, Log.INFO)
            }
            ILogger.CATEGORY_PRIVATE -> {
                log(currentTag, msg, Log.DEBUG)
            }
            else -> {
                log(currentTag, msg, Log.DEBUG)
            }
        }
    }

    override fun logError(throwable: Throwable) {
        logError(Log.getStackTraceString(throwable))
    }

    override fun logError(error: M) {
        logError("ErrorModel{code = ${error.code}, statusEnum = ${error.statusEnum.name}}")
    }

    override fun sendLogs(msg: String) {
        logCategory(ILogger.CATEGORY_IMPORTANT, currentTag, "Method sendLogs() is not implemented. Message: $msg")
    }

    override fun showMessage(msg: String, length: Int) {
        logInfo(msg)
        Toast.makeText(context, msg, length).show()
    }

    override fun showMessage(msgRes: Int, length: Int) {
        if (context != null) {
            showMessage(context?.resources?.getString(msgRes) ?: "Resource not found or context is not set", length)
        } else {
            logImportant("showMessage: context is null, msg was not showed")
        }
    }
}