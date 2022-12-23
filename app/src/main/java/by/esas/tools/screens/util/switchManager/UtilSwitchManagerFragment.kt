package by.esas.tools.screens.util.switchManager

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainUtilSwitchManagerBinding
import by.esas.tools.util.SwitchManager

class UtilSwitchManagerFragment: AppFragment<UtilSwitchManagerVM, FMainUtilSwitchManagerBinding>() {
    override val fragmentDestinationId = R.id.utilSwitchManagerFragment

    override fun provideLayoutId() = R.layout.f_main_util_switch_manager

    override fun provideViewModel(): UtilSwitchManagerVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(UtilSwitchManagerVM::class.java)
    }

    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fMainUtilManagerBaseButton,
            binding.fMainUtilManagerBaseEdit,
            binding.fMainUtilManagerBaseTestSwitcher,
            binding.fMainUtilManagerBaseSwitchMaterial
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainUtilManagerBaseSwitcher.setSwitchHandler(object : ISwitchHandler {
            override fun onSwitchChange(isChecked: Boolean) {
                if (isChecked)
                    enableControls()
                else
                    disableControls()
            }
        })
        binding.fMainUtilManagerBaseSwitcher.switcherIsChecked(true)

        binding.fMainUtilManagerCustomSwitcher.setSwitchHandler(object : ISwitchHandler {
            override fun onSwitchChange(isChecked: Boolean) {
                val switcher = provideSwitchManagerForCustomSwitcher()
                provideViewsForCustomSwitcher().forEach { view ->
                    view?.let {
                        if (isChecked)
                            switcher.enableView(view)
                        else
                            switcher.disableView(view)
                    }
                }
            }
        })
        binding.fMainUtilManagerCustomSwitcher.switcherIsChecked(true)

        binding.fMainUtilManagerBaseButton.setOnClickListener {
            Toast.makeText(requireContext(), resources.getString(R.string.util_manager_button_click), Toast.LENGTH_SHORT).show()
        }
        binding.fMainUtilManagerCustomButton.setOnClickListener {
            Toast.makeText(requireContext(), resources.getString(R.string.util_manager_button_click), Toast.LENGTH_SHORT).show()
        }
    }

    private fun provideSwitchManagerForCustomSwitcher(): SwitchManager {
        return object : SwitchManager() {
            override fun enableView(view: View): Boolean {
                return when (view){
                    is Button, is SwitchCompat -> {
                        view.isEnabled = true
                        true
                    }
                    else ->
                        super.enableView(view)
                }
            }

            override fun disableView(view: View): Boolean {
                return when (view){
                    is Button, is SwitchCompat -> {
                        view.isEnabled = false
                        false
                    }
                    else ->
                        super.disableView(view)
                }
            }
        }
    }

    private fun provideViewsForCustomSwitcher(): List<View?> {
        return listOf(
            binding.fMainUtilManagerCustomButton,
            binding.fMainUtilManagerCustomEdit,
            binding.fMainUtilManagerCustomTestSwitcher,
            binding.fMainUtilManagerCustomSwitchMaterial
        )
    }
}
