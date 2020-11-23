package by.esas.tools.basedaggerui.app

import android.content.Context

interface BaseApp {
        fun setAppContext(context:Context)
        fun getAppContext():Context
}