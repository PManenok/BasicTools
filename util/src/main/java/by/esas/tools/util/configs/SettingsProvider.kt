/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util.configs

interface SettingsProvider {
    // language methods
    fun getDefaultLanguage(): String
    fun getLanguage(): String
    fun setLanguage(lang: String)

    fun getDefaultMode(): UiModeType
    fun getMode(): UiModeType
    fun setMode(uiMode: UiModeType)
}