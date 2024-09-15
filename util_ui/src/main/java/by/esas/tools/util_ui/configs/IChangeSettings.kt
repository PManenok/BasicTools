/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util_ui.configs

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

interface IChangeSettings {

    fun doWithAttachBaseContext(context: Context): Context {
        val setter = provideSetter()
        val lang = setter.getLanguage()
        val mode = setter.getMode()
        logInfo("doWithAttachBaseContext language $lang, mode $mode")
        return SettingsManager.updateSettings(context, lang, mode) ?: context
    }

    /**
     * This method changes language of the app if it is not the same as previous language
     * and then recreates activity
     * */
    fun changeLanguage(lang: String = "") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val setter = provideSetter()
            val prevLang = setter.getLanguage()
            val newLang: String = lang.ifBlank { setter.getDefaultLanguage() }
            if (newLang != prevLang) {
                logInfo("changeLanguage from $prevLang to $newLang($lang)")
                setter.setLanguage(lang)
                val appContext = getAppContext()
                val config = Configuration(appContext.resources?.configuration)
                SettingsManager.localeSettings(config, lang)
                val newContext = appContext.createConfigurationContext(config)
                setAppContext(newContext)
                recreateActivity()
            }
        }
    }

    /**
     * This method changes app's theme to one of [AppCompatDelegate.MODE_NIGHT_NO],
     * [AppCompatDelegate.MODE_NIGHT_YES] or [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM].
     * If new theme is different from previous(current) one then [AppCompatDelegate.setDefaultNightMode]
     * will be called
     * */
    fun changeNightMode(value: String = "") {
        val setter = provideSetter()
        val newMode = UiModeType.getMode(value) ?: setter.getDefaultMode()
        val savedMode = setter.getMode()
        if (newMode != savedMode) {
            logInfo("changeNightMode from ${savedMode.name} to ${newMode.name}($value)")
            setter.setMode(newMode)
            val uiMode = when (newMode) {
                UiModeType.DAY -> AppCompatDelegate.MODE_NIGHT_NO
                UiModeType.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(uiMode)
        }
    }

    fun provideSetter(): SettingsProvider
    fun recreateActivity()
    fun getAppContext(): Context
    fun setAppContext(context: Context)
    fun logInfo(info: String)
}