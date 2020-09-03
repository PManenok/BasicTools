package by.esas.tools.baseui.interfaces

import androidx.lifecycle.MutableLiveData

interface IChangeLangVM {
    val changeLang: MutableLiveData<String?>

    fun onChangeLanguage()
}