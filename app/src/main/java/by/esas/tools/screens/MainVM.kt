package by.esas.tools.screens

import androidx.databinding.ObservableBoolean
import by.esas.tools.base.AppVM
import javax.inject.Inject

class MainVM @Inject constructor() : AppVM() {

    val hasBackBtn = ObservableBoolean(false)

}

