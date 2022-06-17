/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

import android.util.Log
import android.widget.Toast

interface ILogger<E : Enum<E>, M : IErrorModel<E>> {
    var currentTag: String

    companion object {
        const val CATEGORY_DEBUG: String = "CATEGORY_DEBUG"
        const val CATEGORY_INFO: String = "CATEGORY_INFO"
        const val CATEGORY_IMPORTANT: String = "CATEGORY_IMPORTANT"
        const val CATEGORY_ORDER: String = "CATEGORY_ORDER"
        const val CATEGORY_ERROR: String = "CATEGORY_ERROR"
        const val CATEGORY_USER: String = "CATEGORY_USER"
        const val CATEGORY_PRIVATE: String = "CATEGORY_PRIVATE"
    }

    fun setTag(tag: String) {
        this.currentTag = tag
    }

    fun log(tag: String, msg: String, level: Int = Log.DEBUG)

    fun logCategory(category: String, tag: String, msg: String)

    fun logError(throwable: Throwable)

    fun logError(error: M)

    fun logError(msg: String) {
        logCategory(CATEGORY_ERROR, currentTag, msg)
    }

    fun logInfo(msg: String) {
        logCategory(CATEGORY_INFO, currentTag, msg)
    }

    fun logImportant(msg: String) {
        logCategory(CATEGORY_IMPORTANT, currentTag, msg)
    }

    fun logOrder(msg: String) {
        logCategory(CATEGORY_ORDER, currentTag, msg)
    }

    fun logDebug(msg: String) {
        logCategory(CATEGORY_DEBUG, currentTag, msg)
    }
    fun logUserInfo(msg: String) {
        logCategory(CATEGORY_USER, currentTag, msg)
    }
    fun logPrivate(msg: String) {
        logCategory(CATEGORY_PRIVATE, currentTag, msg)
    }

    fun sendLogs(msg: String = "Logs")

    fun showMessage(msg: String, length: Int = Toast.LENGTH_SHORT)

    fun showMessage(msgRes: Int, length: Int = Toast.LENGTH_SHORT)
}