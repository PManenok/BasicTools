/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util

import java.util.*

interface SettingsProvider {
    // language methods
    fun getDefaultLanguage(): String
    fun getLanguage(): String
    fun setLanguage(lang: String)
    fun getCurrentLocale(): Locale {
        return Locale(getLanguage())
    }
}