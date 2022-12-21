package by.esas.tools.screens.dialog

import androidx.databinding.ObservableBoolean
import by.esas.tools.base.AppVM
import javax.inject.Inject

class DynamicMessageDialogVM @Inject constructor(): AppVM() {
    val btnNeutralLayVisibility = ObservableBoolean(false)
    val btnPositiveLayVisibility = ObservableBoolean(false)
    val btnNegativeLayVisibility = ObservableBoolean(false)
}