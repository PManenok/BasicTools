package by.esas.tools.app_data

import android.content.Context
import android.content.SharedPreferences
import by.esas.tools.util.configs.UiModeType

class AppSharedPrefs(val context: Context) {
    private val sharedPrefs: SharedPreferences

    companion object {
        private const val CURRENT_LANGUAGE = "klangkopl"
        private const val CURRENT_THEME = "pjuthemred"
        private const val SHARED_PREFS_NAME = "basic_tools_pref"
    }

    init {
        sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setLanguage(lang: String) {
        sharedPrefs.edit().putString(CURRENT_LANGUAGE, lang).apply()
    }

    fun getLanguage(): String {
        return sharedPrefs.getString(CURRENT_LANGUAGE, "en") ?: "en"
    }

    fun setTheme(theme: UiModeType) {
        sharedPrefs.edit().putString(CURRENT_THEME, theme.name).apply()
    }

    fun getTheme(): UiModeType {
        val currentTheme = sharedPrefs.getString(CURRENT_THEME, "") ?: ""
        return if (currentTheme.isNotBlank()){
            UiModeType.valueOf(currentTheme)
        } else UiModeType.SYSTEM
    }
}