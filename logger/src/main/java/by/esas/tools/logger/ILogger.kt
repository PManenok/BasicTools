/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

import android.util.Log
import android.widget.Toast

interface ILogger<E : Enum<E>, M : IErrorModel<E>> {
    fun setTag(tag: String)

    fun showMessage(msg: String, length: Int = Toast.LENGTH_SHORT)

    fun showMessage(msgRes: Int, length: Int = Toast.LENGTH_SHORT)

    fun logLocally(msg: String)

    fun log(msg: String)

    fun log(tag: String, msg: String, level: Int = Log.DEBUG)

    fun logError(throwable: Throwable)

    fun logError(error: M)

    fun logError(msg: String)

    fun logInfo(msg: String)

    fun sendLogs(msg: String = "Logs")
}