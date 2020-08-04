package by.esas.basictools.utils.extra

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleManager {
    fun setLocale(c:Context, lang:String): Context {
        return setNewLocale(c, lang)
    }

    private fun setNewLocale(c: Context, language: String): Context {
        //persistLanguage(c, language)
        return updateResources(c, language)
    }

    /*private fun persistLanguage(c: Context, language: String) {

    }*/

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}