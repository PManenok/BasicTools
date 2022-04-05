package by.esas.tools.basedaggerui.interfaces

import android.os.Bundle
import androidx.navigation.NavDirections
import by.esas.tools.basedaggerui.navigating.PopBackEntity

interface INavigateVM {
    fun navigate(direction: NavDirections)
    fun popBack()
    fun popBack(entity: PopBackEntity, parameters: Bundle? = null)
}