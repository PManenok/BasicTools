package by.esas.tools.screens.numpad.numpad_text

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainNumpadTextBinding
import by.esas.tools.numpad.INumPadHandler
import by.esas.tools.util.SwitchManager
import by.esas.tools.dpToPx
import com.google.android.material.button.MaterialButton

class NumpadTextFragment : AppFragment<NumpadTextVM, FMainNumpadTextBinding>() {
    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fMainNumpadText,
            binding.fMainNumpadTextStyleChangeButton,
            binding.fMainNumpadTextIconsIncreaseButton,
            binding.fMainNumpadTextIconsDecreaseButton
        )
    }

    override var switcher: SwitchManager = object : SwitchManager(){
        override fun enableView(view: View): Boolean {
            return if (view is MaterialButton || view is ImageButton){
                view.isEnabled = true
                true
            } else {
                super.enableView(view)
            }
        }

        override fun disableView(view: View): Boolean {
            return if (view is MaterialButton || view is ImageButton){
                view.isEnabled = false
                true
            } else {
                super.disableView(view)
            }
        }
    }

    override val fragmentDestinationId = R.id.numpadTextFragment
    override fun provideLayoutId() = R.layout.f_main_numpad_text

    override fun provideViewModel(): NumpadTextVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(NumpadTextVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainNumpadTextSwitcher.switcherIsChecked(true)
        binding.fMainNumpadTextSwitcher.setSwitchHandler(object : ISwitchHandler {
            override fun onSwitchChange(isChecked: Boolean) {
                if (isChecked)
                    enableControls()
                else
                    disableControls()
            }
        })

        binding.fMainNumpadText.setNumpadHandler(object : INumPadHandler{
            override fun onNumClick(num: Int) {
                viewModel.onIconClick(num)
            }

            override fun onLeftIconClick() {
                viewModel.onCancelClick()
            }

            override fun onRightIconClick() {
                viewModel.onRestoreClick()
            }
        })

    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.iconsParamsLive.observe(this) { params ->
            binding.fMainNumpadText.apply {
                setIconsSize(dpToPx(params.iconSize), dpToPx(params.iconSize))
                setNumbersTextSize(params.numTextSize)
                setLeftIconSize(dpToPx(params.imageSize), dpToPx(params.imageSize))
                setRightIconSize(dpToPx(params.imageSize), dpToPx(params.imageSize))
            }
        }
        viewModel.iconsIsDefaultLive.observe(this) { isDefault ->
            if (isDefault)
                binding.fMainNumpadText.setNumbersDefaultStyle()
            else
                binding.fMainNumpadText.setNumbersTextStyle(R.style.NumpadTextNumbersStyle)
        }
    }
}
