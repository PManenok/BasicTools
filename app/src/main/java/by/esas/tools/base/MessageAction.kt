package by.esas.tools.base

import android.os.Bundle
import android.widget.Toast
import by.esas.tools.logger.Action

class MessageAction : Action {

    constructor(text: String, duration: Int) : super(
        ACTION_SHOW_MESSAGE,
        Bundle(),
        false
    ) {
        parameters?.putString(SHOW_MESSAGE_TEXT, text)
        parameters?.putInt(SHOW_MESSAGE_DURATION, duration)
    }

    constructor(resId: Int, duration: Int) : super(
        ACTION_SHOW_MESSAGE,
        Bundle(),
        false
    ) {
        parameters?.putInt(SHOW_MESSAGE_RES, resId)
        parameters?.putInt(SHOW_MESSAGE_DURATION, duration)
    }

    fun getMessage(): String {
        return parameters?.getString(SHOW_MESSAGE_TEXT) ?: ""
    }

    fun getResId(): Int {
        return parameters?.getInt(SHOW_MESSAGE_RES) ?: 0
    }

    fun getDuration(): Int {
        return parameters?.getInt(SHOW_MESSAGE_DURATION) ?: Toast.LENGTH_SHORT
    }

    fun hasTextMessage(): Boolean {
        return !parameters?.keySet()?.find {
            it == SHOW_MESSAGE_TEXT
        }.isNullOrBlank()
    }

    companion object {
        const val ACTION_SHOW_MESSAGE: String = "ACTION_SHOW_MESSAGE"
        const val SHOW_MESSAGE_TEXT: String = "SHOW_MESSAGE_TEXT"
        const val SHOW_MESSAGE_RES: String = "SHOW_MESSAGE_RES"
        const val SHOW_MESSAGE_DURATION: String = "SHOW_MESSAGE_DURATION"
    }
}