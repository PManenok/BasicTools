package by.esas.tools.baseui.app

import android.content.Context

interface BaseApp {
    companion object {
        lateinit var appContext: Context
    }
}