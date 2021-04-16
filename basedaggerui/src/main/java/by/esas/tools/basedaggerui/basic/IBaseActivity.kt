/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.basic

import android.content.Context
import by.esas.tools.logger.ILogger
import by.esas.tools.util.LanguageSetter
import by.esas.tools.util.LocaleManager

interface IBaseActivity<E : Enum<E>> {
    var logger: ILogger<E, *>
    fun doWithAttachBaseContext(context: Context): Context {
        return LocaleManager.setLocale(context, provideSetter().getLanguage())
    }

    fun setLanguage(lang: String = "") {
        logger.logInfo("Changing language to $lang")
        val setter = provideSetter()
        val prevLang = setter.getLanguage()
        logger.logInfo("Previous (current) language is $prevLang")
        val newLang: String = if (lang.isNotBlank()) lang else setter.getDefaultLanguage()
        if (newLang != prevLang) {
            setter.setLanguage(lang)
            setAppContext(LocaleManager.setLocale(getAppContext(), lang))
            logger.logInfo("Activity recreate")
            recreateActivity()
        }
    }

    fun provideSetter(): LanguageSetter
    fun recreateActivity()
    fun getAppContext(): Context
    fun setAppContext(context: Context)
}