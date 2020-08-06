package by.esas.tools.logger

import android.util.Log
import android.widget.Toast

interface ILogger<E : Enum<E>> {
    fun setTag(tag: String)

    fun showMessage(msg: String, length: Int = Toast.LENGTH_SHORT)

    fun showMessage(msgRes: Int, length: Int = Toast.LENGTH_SHORT)

    fun logLocally(msg: String)

    fun log(msg: String)

    fun log(tag: String, msg: String, level: Int = Log.DEBUG)

    fun logError(throwable: Throwable)

    fun logError(error: IErrorModel<E>)

    fun logError(msg: String)

    fun logInfo(msg: String)

    fun sendLogs(msg: String = "Logs")
}