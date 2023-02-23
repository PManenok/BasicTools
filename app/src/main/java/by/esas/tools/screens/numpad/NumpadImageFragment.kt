package by.esas.tools.screens.numpad

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainNumpadImageBinding
import by.esas.tools.dpToPx
import by.esas.tools.numpad.INumPadHandler
import by.esas.tools.util.SwitchManager
import com.google.android.material.button.MaterialButton

class NumpadImageFragment : AppFragment<NumpadImageVM, FMainNumpadImageBinding>() {

    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fMainNumpadImage,
            binding.fMainNumpadIconsChangeButton,
            binding.fMainNumpadIconsIncreaseButton,
            binding.fMainNumpadIconsDecreaseButton
        )
    }

    override var switcher: SwitchManager = object : SwitchManager() {
        override fun enableView(view: View): Boolean {
            return if (view is MaterialButton || view is ImageButton) {
                view.isEnabled = true
                true
            } else {
                super.enableView(view)
            }
        }

        override fun disableView(view: View): Boolean {
            return if (view is MaterialButton || view is ImageButton) {
                view.isEnabled = false
                true
            } else {
                super.disableView(view)
            }
        }
    }

    override val fragmentDestinationId = R.id.numpadImageFragment

    override fun provideLayoutId() = R.layout.f_main_numpad_image

    override fun provideViewModel(): NumpadImageVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(NumpadImageVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainNumpadSwitcher.switcherIsChecked(true)
        binding.fMainNumpadSwitcher.setSwitchHandler(object : ISwitchHandler {
            override fun onSwitchChange(isChecked: Boolean) {
                if (isChecked)
                    viewModel.enableControls()
                else
                    viewModel.disableControls()
            }
        })
        binding.fMainNumpadImage.setNumpadHandler(object : INumPadHandler {
            override fun onNumClick(num: Int) {
                viewModel.onIconClick(num)
            }

            override fun onRightIconClick() {
                viewModel.onRestoreClick()
            }

            override fun onLeftIconClick() {
                viewModel.onCancelClick()
            }
        })
        binding.fMainNumpadImage.setIconsContainersPaddings(
            dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10)
        )
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.iconsSizeLive.observe(this) { size ->
            binding.fMainNumpadImage.setIconsSize(dpToPx(size))
        }
        viewModel.iconsIsDefaultLive.observe(this) { isDefault ->
            if (isDefault) {
                binding.fMainNumpadImage.setDefaultNumpadIcons()
            } else {
                binding.fMainNumpadImage.apply {
                    setNumbersIconsImageResources(viewModel.numpadIconsList)
                    setRightIconImageResource(viewModel.numpadRightIconImage)
                    setLeftIconImageResource(viewModel.numpadLeftIconImage)
                }
            }
        }
    }
}
