package by.esas.tools.screens.custom_switch

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainCustomSwitchBinding

class CustomSwitchFragment: AppFragment<CustomSwitchVM, FMainCustomSwitchBinding>() {
    override val fragmentDestinationId = R.id.customSwitchFragment
    override fun provideLayoutId() = R.layout.f_main_custom_switch

    override fun provideViewModel(): CustomSwitchVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(CustomSwitchVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainSwitcher.setSwitchHandler(object : ISwitchHandler{

            override fun onSwitchChange(isChecked: Boolean) {
                if (isChecked)
                    Toast.makeText(context, "Switcher is on", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "Switcher is off", Toast.LENGTH_SHORT).show()
            }

            override fun prepareToSwitchOn(): Boolean {
                return binding.fMainSwitcherEditField.text.isNotEmpty()
            }
        })
    }
}
