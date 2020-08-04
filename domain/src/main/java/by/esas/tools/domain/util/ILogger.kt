package by.esas.tools.domain.util

import android.util.Log
import android.widget.Toast
import by.esas.tools.domain.mapper.ErrorModel

interface ILogger<E : Enum<E>> {
    fun setTag(tag: String)

    fun showMessage(msg: String, length: Int = Toast.LENGTH_SHORT)

    fun showMessage(msgRes: Int, length: Int = Toast.LENGTH_SHORT)

    fun logLocally(msg: String)

    fun log(msg: String)

    fun log(tag: String, msg: String, level: Int = Log.DEBUG)

    fun logError(throwable: Throwable)

    fun logError(error: ErrorModel<E>)

    fun logError(msg: String)

    fun logInfo(msg: String)

    fun sendLogs(msg: String = "Logs")
}