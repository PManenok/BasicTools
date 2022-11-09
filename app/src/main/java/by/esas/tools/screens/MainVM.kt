package by.esas.tools.screens

import androidx.lifecycle.MutableLiveData
import by.esas.tools.base.AppVM
import javax.inject.Inject

class MainVM @Inject constructor() : AppVM() {

    val hasBackBtn = MutableLiveData(false)
    val title = MutableLiveData("")
}

