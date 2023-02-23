package by.esas.tools.screens.custom_switch.program

import android.os.Bundle
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.View.TEXT_ALIGNMENT_TEXT_END
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.CustomSwitch
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainCustomSwitchProgramBinding
import by.esas.tools.inputfieldview.dpToPx

class CustomSwitchProgramFragment : AppFragment<CustomSwitchProgramVM, FMainCustomSwitchProgramBinding>() {

    override val fragmentDestinationId = R.id.customSwitchProgramFragment

    override fun provideLayoutId() = R.layout.f_main_custom_switch_program

    override fun provideViewModel(): CustomSwitchProgramVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory())[CustomSwitchProgramVM::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fSwitcherProgramButtonCreate.setOnClickListener {
            binding.fSwitcherProgramContainer.removeAllViews()
            val customSwitch = createCustomSwitch()
            customSwitch.setSwitchHandler(object : ISwitchHandler {
                override fun onSwitchChange(isChecked: Boolean) {
                    val message = if (isChecked) resources.getString(R.string.custom_switch_status_on)
                    else resources.getString(R.string.custom_switch_status_off)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
            binding.fSwitcherProgramContainer.addView(customSwitch)
        }
    }

    private fun createCustomSwitch(): CustomSwitch {
        val customSwitch = CustomSwitch(requireContext())
        customSwitch.setDefaultValue()

        customSwitch.setSwitchThumbTint(getThumdColor())
        customSwitch.setSwitchTrackTint(getTrackColor())

        customSwitch.setTitle(binding.fSwitcherProgramTitle.getText())
        val titleStyle = getTextStyle(binding.fSwitcherProgramSpinnerTitle.selectedItem.toString())
        customSwitch.setTitleStyle(titleStyle)
        customSwitch.setInfo(binding.fSwitcherProgramInfo.getText())
        val infoStyle = getTextStyle(binding.fSwitcherProgramSpinnerInfo.selectedItem.toString())
        customSwitch.setInfoStyle(infoStyle)
        customSwitch.setInfoAlignment(getInfoAlignment())

        setSwitcherPaddings(customSwitch)
        setInfoPaddings(customSwitch)
        return customSwitch
    }

    private fun getThumdColor(): Int {
        val checkedButtonId = binding.fSwitcherProgramThumbRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fSwitcherProgramThumbRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when (checkedButton) {
            binding.fSwitcherProgramThumbRadio1 -> R.color.switcher_selector_orange
            binding.fSwitcherProgramThumbRadio2 -> R.color.switcher_thumb_selector
            binding.fSwitcherProgramThumbRadio3 -> R.color.switcher_selector_red
            binding.fSwitcherProgramThumbRadio4 -> R.color.switcher_selector_green
            else -> R.color.switcher_selector_yellow
        }
    }

    private fun getTrackColor(): Int {
        val checkedButtonId = binding.fSwitcherProgramTrackRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fSwitcherProgramTrackRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when (checkedButton) {
            binding.fSwitcherProgramTrackRadio1 -> R.color.switcher_selector_orange_light
            binding.fSwitcherProgramTrackRadio2 -> R.color.switcher_selector
            binding.fSwitcherProgramTrackRadio3 -> R.color.switcher_selector_holo_red
            binding.fSwitcherProgramTrackRadio4 -> R.color.switcher_selector_green_light
            else -> R.color.switcher_selector_yellow_light
        }
    }

    private fun getInfoAlignment(): Int {
        val alignment = binding.fSwitcherProgramSpinnerAlignment.selectedItem.toString()
        return when (alignment) {
            resources.getString(R.string.alignment_left) -> TEXT_ALIGNMENT_TEXT_START
            resources.getString(R.string.alignment_center) -> TEXT_ALIGNMENT_CENTER
            else -> TEXT_ALIGNMENT_TEXT_END
        }
    }

    private fun setSwitcherPaddings(customSwitch: CustomSwitch) {
        val left = getPaddings(binding.fSwitcherProgramPaddingsSwitcherLeft.getText())
        val top = getPaddings(binding.fSwitcherProgramPaddingsSwitcherTop.getText())
        val right = getPaddings(binding.fSwitcherProgramPaddingsSwitcherRight.getText())
        val bottom = getPaddings(binding.fSwitcherProgramPaddingsSwitcherBottom.getText())

        customSwitch.setSwitchPaddings(left, top, right, bottom)
    }

    private fun setInfoPaddings(customSwitch: CustomSwitch) {
        val left = getPaddings(binding.fSwitcherProgramPaddingsInfoLeft.getText())
        val top = getPaddings(binding.fSwitcherProgramPaddingsInfoTop.getText())
        val right = getPaddings(binding.fSwitcherProgramPaddingsInfoRight.getText())
        val bottom = getPaddings(binding.fSwitcherProgramPaddingsInfoBottom.getText())

        customSwitch.setInfoPaddings(left, top, right, bottom)
    }

    private fun getPaddings(paddings: String): Int {
        val paddings = if (paddings.isNotEmpty()) paddings.toInt() else 0
        return dpToPx(paddings).toInt()
    }

    private fun getTextStyle(style: String): Int {
        return when (style) {
            resources.getString(R.string.style_1) -> R.style.CustomSwitcherTitleTextStyle
            resources.getString(R.string.style_2) -> R.style.CustomSwitcherTextStyleBold
            else -> R.style.CustomSwitcherTextStyleNormal
        }
    }
}
