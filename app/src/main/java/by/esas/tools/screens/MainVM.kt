package by.esas.tools.screens

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import by.esas.tools.base.AppVM
import javax.inject.Inject

class MainVM @Inject constructor() : AppVM() {

    val header = ObservableField<String>("")
    val hasBackBtn = ObservableBoolean(false)

}

