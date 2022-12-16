package by.esas.tools.screens

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.base.AppVM
import javax.inject.Inject

class MainVM @Inject constructor() : AppVM() {

    val hasBackBtn = ObservableBoolean(false)
    val hasSettingsBtn = ObservableBoolean(true)
    val title = ObservableField(App.appContext.resources.getString(R.string.menu))
}
