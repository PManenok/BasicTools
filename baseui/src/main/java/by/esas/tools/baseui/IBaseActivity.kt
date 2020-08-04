package by.esas.tools.baseui

import android.content.Context
import by.esas.basictools.utils.extra.LanguageSetter
import by.esas.basictools.utils.extra.LocaleManager
import by.esas.basictools.utils.logger.BaseLogger

interface IBaseActivity {
    fun doWithAttachBaseContext(context: Context): Context {
        return LocaleManager.setLocale(context, provideSetter().getLanguage())
    }

    fun setLanguage(lang: String = "") {
        val logger = provideLogger()
        logger.logInfo(provideTAG(), "Changing language to $lang")
        val setter = provideSetter()
        val prevLang = setter.getLanguage()
        logger.logInfo(provideTAG(), "Previous (current) language is $prevLang")
        val newLang: String = if (lang.isNotBlank()) lang else setter.getDefaultLanguage()
        if (newLang != prevLang) {
            setter.setLanguage(lang)
            setAppContext(LocaleManager.setLocale(getAppContext(), lang))
            logger.logInfo(provideTAG(), "Activity recreate")
            recreateActivity()
        }
    }

    fun provideTAG(): String
    fun provideLogger(): BaseLogger
    fun provideSetter(): LanguageSetter
    fun recreateActivity()
    fun getAppContext(): Context
    fun setAppContext(context: Context)
}