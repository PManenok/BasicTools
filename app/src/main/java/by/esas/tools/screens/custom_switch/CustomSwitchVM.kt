package by.esas.tools.screens.custom_switch

import androidx.lifecycle.MutableLiveData
import by.esas.tools.base.AppVM
import javax.inject.Inject

class CustomSwitchVM @Inject constructor(): AppVM() {
    val styleIsNew = MutableLiveData<Boolean>()

    fun updateSwitcherStyle(){
        styleIsNew.value = if (styleIsNew.value != null ) !styleIsNew.value!! else true
    }
}