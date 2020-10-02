package by.esas.tools.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object LocaleManager {
    /**
     * This method sets locale only since API 17
     * From lower api it does nothing and return the same context
     **/
    fun setLocale(c: Context, lang: String): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setNewLocale(c, lang)
        } else {
            c
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setNewLocale(c: Context, language: String): Context {
        return updateResources(c, language)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}