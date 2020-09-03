package by.esas.tools.util

import java.util.*

interface LanguageSetter {
    fun getDefaultLanguage(): String
    fun getLanguage(): String
    fun setLanguage(lang: String)
    fun getCurrentLocale(): Locale {
        return Locale(getLanguage())
    }
}