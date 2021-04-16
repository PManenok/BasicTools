/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.interfaces

import androidx.lifecycle.MutableLiveData

interface IChangeLangVM {
    val changeLang: MutableLiveData<String?>

    fun onChangeLanguage()
}