package by.esas.tools.screens.custom_switch

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.CustomSwitch
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainCustomSwitchBinding
import by.esas.tools.util.defocusAndHideKeyboard

class CustomSwitchFragment : AppFragment<CustomSwitchVM, FMainCustomSwitchBinding>() {
    override val fragmentDestinationId = R.id.customSwitchFragment
    override fun provideLayoutId() = R.layout.f_main_custom_switch

    override fun provideViewModel(): CustomSwitchVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(CustomSwitchVM::class.java)
    }

    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fMainSwitcherCheck,
            binding.fMainSwitcherCheckField,
            binding.fMainSwitcherStyle,
            binding.fMainSwitcherEditTitle,
            binding.fMainSwitcherEditInfo
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainSwitcherCheck.setSwitchHandler(object : ISwitchHandler {
            override fun onSwitchChange(isChecked: Boolean) {
                val message = if (isChecked) resources.getString(R.string.custom_switch_status_on)
                else resources.getString(R.string.custom_switch_status_off)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            override fun prepareToSwitchOn(): Boolean {
                return binding.fMainSwitcherCheckField.getText().isNotEmpty()
            }
        })

        binding.fMainSwitcherEditTitle.inputText?.setOnEditorActionListener { titleView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.fMainSwitcherStyle.setTitle(titleView.text.toString())
                defocusAndHideKeyboard(activity)
                true
            } else
                false
        }

        binding.fMainSwitcherEditInfo.inputText?.setOnEditorActionListener { titleView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.fMainSwitcherStyle.setInfo(titleView.text.toString())
                true
            } else
                false
        }

        binding.fMainSwitcherChangeStyleButton.setOnClickListener {
            setCustomSwitcherStyle()
            it.isEnabled = false
        }

        binding.fMainSwitcherLay.addView(createDisableSwitcher())
    }

    private fun createDisableSwitcher(): CustomSwitch {
        val newSwitcher = CustomSwitch(requireContext())
        newSwitcher.apply {
            setTitle(resources.getString(R.string.custom_switch_disable_title))
            setTitleStyle(R.style.TextStyle)
            setInfo(resources.getString(R.string.custom_switch_disable_instruction))
            setInfoStyle(R.style.HintTextStyle)
            setDefaultValue()
            switcherIsChecked(true)
            setSwitchHandler(object : ISwitchHandler {
                override fun onSwitchChange(isChecked: Boolean) {
                    when (isChecked) {
                        true -> enableControls()
                        false -> disableControls()
                    }
                }
            })
        }
        return newSwitcher
    }

    private fun setCustomSwitcherStyle() {
        binding.fMainSwitcherStyle.apply {
            setSwitchTrackTint(R.color.switcher_selector)
            setSwitchThumbTint(R.color.switcher_thumb_selector)
            setTitleStyle(R.style.CustomSwitcherTitleTextStyle)
            setInfoStyle(R.style.CustomSwitcherInfoTextStyle)
        }
    }
}
