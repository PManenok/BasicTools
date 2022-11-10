package by.esas.tools.screens.baseui.navigation

import by.esas.tools.base.AppVM
import javax.inject.Inject

class BaseUINavigationVM @Inject constructor(): AppVM() {
    fun navigateToSecondFragment(date: String = "") {
        navigate(BaseUINavigationFragmentDirections.actionMenuFragmentToBaseuiNavigationSecondFragment(
            date = date
        ))
    }
}
