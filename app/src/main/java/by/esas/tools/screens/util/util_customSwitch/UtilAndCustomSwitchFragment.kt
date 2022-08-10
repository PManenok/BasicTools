package by.esas.tools.screens.util.util_customSwitch

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainUtilCustomSwitchBinding

class UtilAndCustomSwitchFragment :
    AppFragment<UtilAndCustomSwitchVM, FMainUtilCustomSwitchBinding>() {
    override val fragmentDestinationId = R.id.utilAndCustomSwitchFragment

    override fun provideLayoutId() = R.layout.f_main_util_custom_switch

    override fun provideViewModel(): UtilAndCustomSwitchVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(UtilAndCustomSwitchVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val customSwitch = context?.let { CustomSwitch(it) }
//        binding.lay.addView(customSwitch)
    }
}
