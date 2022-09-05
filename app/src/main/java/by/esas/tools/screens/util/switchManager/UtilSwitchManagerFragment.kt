package by.esas.tools.screens.util.switchManager

import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainUtilSwitchManagerBinding

class UtilSwitchManagerFragment: AppFragment<UtilSwitchManagerVM, FMainUtilSwitchManagerBinding>() {
    override val fragmentDestinationId = R.id.utilSwitchManagerFragment

    override fun provideLayoutId() = R.layout.f_main_util_switch_manager

    override fun provideViewModel(): UtilSwitchManagerVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(UtilSwitchManagerVM::class.java)
    }

}