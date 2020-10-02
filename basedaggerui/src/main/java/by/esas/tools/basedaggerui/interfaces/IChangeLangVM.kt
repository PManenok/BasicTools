package by.esas.tools.basedaggerui.interfaces

import androidx.lifecycle.MutableLiveData

interface IChangeLangVM {
    val changeLang: MutableLiveData<String?>

    fun onChangeLanguage()
}