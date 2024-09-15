package by.esas.tools.screens.custom_switch

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.CustomSwitch
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainCustomSwitchBinding
import by.esas.tools.util_ui.SwitchManager
import by.esas.tools.util_ui.defocusAndHideKeyboard

class CustomSwitchFragment : AppFragment<CustomSwitchVM, FMainCustomSwitchBinding>() {

    override val fragmentDestinationId = R.id.customSwitchFragment
    override fun provideLayoutId() = R.layout.f_main_custom_switch

    override fun provideViewModel(): CustomSwitchVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(CustomSwitchVM::class.java)
    }

    override var switcher: by.esas.tools.util_ui.SwitchManager = object : by.esas.tools.util_ui.SwitchManager() {
        override fun enableView(view: View): Boolean {
            return if (view is Button) {
                view.isEnabled = true
                true
            } else
                super.enableView(view)
        }

        override fun disableView(view: View): Boolean {
            return if (view is Button) {
                view.isEnabled = false
                true
            } else
                super.disableView(view)
        }
    }

    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fMainSwitcherCheck,
            binding.fMainSwitcherCheckField,
            binding.fMainSwitcherStyle,
            binding.fMainSwitcherEditTitle,
            binding.fMainSwitcherEditInfo,
            binding.fMainSwitcherEditTitleBtn,
            binding.fMainSwitcherEditInfoBtn,
            binding.fMainSwitcherChangeStyleButton
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
                by.esas.tools.util_ui.defocusAndHideKeyboard(activity)
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

        binding.fMainSwitcherEditTitleBtn.setOnClickListener {
            binding.fMainSwitcherStyle.setTitle(binding.fMainSwitcherEditTitle.getText())
        }
        binding.fMainSwitcherEditInfoBtn.setOnClickListener {
            binding.fMainSwitcherStyle.setInfo(binding.fMainSwitcherEditInfo.getText())
        }

        binding.fMainSwitcherLay.addView(createDisableSwitcher())
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.styleIsNew.observe(viewLifecycleOwner) {
            setCustomSwitcherStyle(it)
        }
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
                    if (isChecked) viewModel.enableControls()
                    else viewModel.disableControls()
                }
            })
        }
        return newSwitcher
    }

    private fun setCustomSwitcherStyle(isNew: Boolean) {
        if (isNew)
            binding.fMainSwitcherStyle.apply {
                setSwitchTrackTint(R.color.switcher_selector)
                setSwitchThumbTint(R.color.switcher_thumb_selector)
                setTitleStyle(R.style.CustomSwitcherTitleTextStyle)
                setInfoStyle(R.style.CustomSwitcherInfoTextStyle)
            }
        else
            binding.fMainSwitcherStyle.apply {
                setSwitchTrackTint(R.color.switcher_selector_yellow_light)
                setSwitchThumbTint(R.color.switcher_selector_yellow)
                setTitleStyle(R.style.CustomSwitcherTextStyleBold)
                setInfoStyle(R.style.CustomSwitcherTextStyleNormal)
            }
    }
}
