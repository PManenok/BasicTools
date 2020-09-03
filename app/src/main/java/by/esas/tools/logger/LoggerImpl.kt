package by.esas.tools.logger

import android.util.Log
import by.esas.tools.error_mapper.AppErrorStatusEnum

class LoggerImpl : ILogger<AppErrorStatusEnum> {
    var baseTag: String = "LoggerImpl"
    override fun setTag(tag: String) {
        this.baseTag = tag
    }

    override fun showMessage(msg: String, length: Int) {

    }

    override fun showMessage(msgRes: Int, length: Int) {

    }

    override fun logLocally(msg: String) {
        Log.w(baseTag, msg)
    }

    override fun log(msg: String) {
        Log.w(baseTag, msg)
    }

    override fun log(tag: String, msg: String, level: Int) {
        Log.w(baseTag, msg)
    }

    override fun logError(throwable: Throwable) {
        Log.e(baseTag, throwable.message, throwable)
    }

    override fun logError(msg: String) {
        Log.e(baseTag, msg)
    }

    override fun logInfo(msg: String) {
        Log.i(baseTag, msg)
    }

    override fun sendLogs(msg: String) {

    }

    override fun logError(error: IErrorModel<AppErrorStatusEnum>) {
        Log.e(baseTag, error.statusEnum.name)
    }
}