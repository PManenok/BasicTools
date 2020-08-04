package by.esas.basictools.utils.extra

interface LanguageSetter {
    fun getDefaultLanguage(): String
    fun getLanguage(): String
    fun setLanguage(lang: String)
}