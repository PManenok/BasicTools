package by.esas.tools.screens.numpad

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainNumpadImageBinding
import by.esas.tools.numpad.INumPadHandler

class NumpadImageFragment : AppFragment<NumpadImageVM, FMainNumpadImageBinding>() {
    override val fragmentDestinationId = R.id.numpadImageFragment

    override fun provideLayoutId() = R.layout.f_main_numpad_image

    override fun provideViewModel(): NumpadImageVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(NumpadImageVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fNumpadImage.handler = object : INumPadHandler{
            override fun onNumClick(num: Int) {
                viewModel.onIconClick(num)
            }

            override fun onRightIconClick() {
                viewModel.onRestoreClick()
            }
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.iconsSizeLive.observe(this){ size ->
            binding.fNumpadImage.setIconsSize(size)
        }
        viewModel.iconsUpdateLive.observe(this) { value ->
            if (value){
                binding.fNumpadImage.apply {
                    setNumbersIconsImageResources(viewModel.iconsList)
                    setRightIconImage(viewModel.numpadRightIconImage)
                }
            }
        }
    }
}
