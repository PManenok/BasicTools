/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.basic

import android.content.Context
import by.esas.tools.logger.ILogger
import by.esas.tools.util.LocaleManager
import by.esas.tools.util.SettingsProvider

interface IChangeSettings<E : Enum<E>> {

    fun doWithAttachBaseContext(context: Context): Context {
        return LocaleManager.setLocale(context, provideSetter().getLanguage())
    }

    fun setLanguage(lang: String = "") {
        provideLogger().logInfo("Changing language to $lang")
        val setter = provideSetter()
        val prevLang = setter.getLanguage()
        provideLogger().logInfo("Previous (current) language is $prevLang")
        val newLang: String = if (lang.isNotBlank()) lang else setter.getDefaultLanguage()
        if (newLang != prevLang) {
            setter.setLanguage(lang)
            setAppContext(LocaleManager.setLocale(getAppContext(), lang))
            provideLogger().logInfo("Activity recreate")
            recreateActivity()
        }
    }

    fun provideLogger(): ILogger<E, *>
    fun provideSetter(): SettingsProvider
    fun recreateActivity()
    fun getAppContext(): Context
    fun setAppContext(context: Context)
}