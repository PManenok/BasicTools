package by.esas.tools.basedaggerui.app

import android.content.Context

interface BaseApp {
    companion object {
        lateinit var appContext: Context
    }
}