package by.esas.tools.logger

import android.content.Context
import android.util.Log
import android.widget.Toast

class BaseLogger<E : Enum<E>>(tag: String? = null, private var context: Context? = null) : ILogger<E, BaseErrorModel<E>> {
    private var TAG: String = tag ?: BaseLogger::class.java.simpleName
    fun setContext(context: Context? = null) {
        this.context = context
    }

    override fun setTag(tag: String) {
        this.TAG = tag
    }

    override fun showMessage(msg: String, length: Int) {
        Log.i(TAG, msg)
        context?.let { Toast.makeText(it, msg, length).show() }
    }

    override fun showMessage(msgRes: Int, length: Int) {
        Log.i(TAG, context?.resources?.getString(msgRes) ?: "Resource not found or context is not set")
        context?.let { Toast.makeText(it, it.resources.getString(msgRes), length).show() }
    }

    override fun logLocally(msg: String) {
        Log.d(TAG, msg)
    }

    override fun log(msg: String) {
        Log.i(TAG, msg)
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

    override fun logError(throwable: Throwable) {
        Log.e(TAG, Log.getStackTraceString(throwable))
    }

    override fun logError(error: BaseErrorModel<E>) {
        Log.e(TAG, "ErrorModel{code = ${error.code}, statusEnum = ${error.statusEnum.name}}")
    }

    override fun logError(msg: String) {
        Log.e(TAG, msg)
    }

    override fun logInfo(msg: String) {
        Log.i(TAG, msg)
    }

    override fun sendLogs(msg: String) {
        Log.w(TAG, "Method sendLogs() is not implemented. Message: $msg")
    }
}