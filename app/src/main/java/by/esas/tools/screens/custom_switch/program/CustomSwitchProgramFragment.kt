package by.esas.tools.screens.custom_switch.program

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.CustomSwitch
import by.esas.tools.databinding.FMainCustomSwitchProgramBinding

class CustomSwitchProgramFragment : AppFragment<CustomSwitchProgramVM, FMainCustomSwitchProgramBinding>() {
    override val fragmentDestinationId = R.id.customSwitchProgramFragment

    override fun provideLayoutId() = R.layout.f_main_custom_switch_program

    override fun provideViewModel(): CustomSwitchProgramVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory())[CustomSwitchProgramVM::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fSwitcherProgramButtonCreate.setOnClickListener {
            val customSwitch = createCustomSwitch()
            binding.fSwitcherProgramContainer.removeAllViews()
            binding.fSwitcherProgramContainer.addView(customSwitch)
        }
    }

    private fun createCustomSwitch(): CustomSwitch {
        val customSwitch = CustomSwitch(requireContext())
        customSwitch.setDefaultValue()

        customSwitch.setSwitchThumbTint(getThumdColor())
        customSwitch.setSwitchTrackTint(getTrackColor())

        customSwitch.setTitle(binding.fSwitcherProgramTitle.text.toString())
        val titleStyle = getTextStyle(binding.fSwitcherProgramSpinnerTitle.selectedItem.toString())
        customSwitch.setTitleStyle(titleStyle)
        customSwitch.setInfo(binding.fSwitcherProgramInfo.text.toString())
        val infoStyle = getTextStyle(binding.fSwitcherProgramSpinnerInfo.selectedItem.toString())
        customSwitch.setInfoStyle(infoStyle)

        return customSwitch
    }

    private fun getThumdColor(): Int {
        val checkedButtonId = binding.fSwitcherProgramThumbRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fSwitcherProgramThumbRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fSwitcherProgramThumbRadio1 -> R.color.gray
            binding.fSwitcherProgramThumbRadio2 -> R.color.purple
            binding.fSwitcherProgramThumbRadio3 -> R.color.red
            binding.fSwitcherProgramThumbRadio4 -> R.color.green
            else -> R.color.yellow
        }
    }

    private fun getTrackColor(): Int {
        val checkedButtonId = binding.fSwitcherProgramTrackRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fSwitcherProgramTrackRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fSwitcherProgramTrackRadio1 -> R.color.colorCursor
            binding.fSwitcherProgramTrackRadio2 -> R.color.purple_light
            binding.fSwitcherProgramTrackRadio3 -> R.color.blue
            binding.fSwitcherProgramTrackRadio4 -> R.color.black
            else -> R.color.colorStrokeOutline
        }
    }

    private fun getTextStyle(style: String): Int {
        return when(style){
            resources.getString(R.string.style_1) -> R.style.CustomSwitcherTitleTextStyle
            resources.getString(R.string.style_2) -> R.style.CustomSwitcherTextStyleBold
            else -> R.style.CustomSwitcherTextStyleNormal
        }
    }
}
