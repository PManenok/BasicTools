package by.esas.tools.util.configs

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

object SettingsManager {
    /**
     * This method is used to update configuration of the provided context.
     *
     * Note! that this method can cause the call of [AppCompatDelegate.setDefaultNightMode] method
     *
     * @param alwaysSystem - decides if theme is forcefully set as [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM]
     * in this case [AppCompatDelegate] will not update default night mode. It should be used
     * in your [android.app.Application] class's attachBaseContext method, so Application theme
     * will not be overridden, otherwise it will cause bugs in work of [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM].
     * */
    fun updateSettings(base: Context?, language: String, uiMode: UiModeType, alwaysSystem: Boolean = false): Context? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && base != null) {
            val config = Configuration(base.resources?.configuration)
            nightModeSettings(config, uiMode, alwaysSystem)
            localeSettings(config, language)
            base.createConfigurationContext(config)
        } else {
            base
        }
    }

    /**
     * This method update ui mode configuration.
     * @param alwaysSystem - decides if theme is forcefully set as [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM]
     * in this case [AppCompatDelegate] will not update default night mode. It should be used
     * in your [android.app.Application] class's attachBaseContext method, so Application theme will not be overridden
     * */
    fun nightModeSettings(config: Configuration, mode: UiModeType, alwaysSystem: Boolean): Configuration {
        val uiMode: Int =
            if (alwaysSystem) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                when (mode) {
                    UiModeType.DAY -> AppCompatDelegate.MODE_NIGHT_NO
                    UiModeType.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
        config.uiMode = when (uiMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> Configuration.UI_MODE_NIGHT_YES
            AppCompatDelegate.MODE_NIGHT_NO -> Configuration.UI_MODE_NIGHT_NO
            else -> Configuration.UI_MODE_TYPE_UNDEFINED
        }

        // when not alwaysSystem (from activity) set default night mode
        // to prevent preferred mode from override for activities
        if (!alwaysSystem && AppCompatDelegate.getDefaultNightMode() != uiMode) {
            AppCompatDelegate.setDefaultNightMode(uiMode)
        }
        return config
    }

    /**
     * This method sets locale only since API 17
     * From lower api it does nothing and return configuration as is.
     * */
    fun localeSettings(config: Configuration, lang: String): Configuration {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val locale: Locale = Locale(lang)
            Locale.setDefault(locale)
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> setLocaleForApi24(config, locale)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> config.setLocale(locale)
            }
        }
        return config
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun setLocaleForApi24(
        config: Configuration,
        locale: Locale
    ) {
        val set: MutableSet<Locale> = LinkedHashSet()
        set.add(locale)
        val all = LocaleList.getDefault()
        for (i in 0 until all.size()) {
            // append other locales supported by the user
            set.add(all[i])
        }
        val locales = set.toTypedArray()
        config.setLocales(LocaleList(*locales))
    }
}