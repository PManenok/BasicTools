package by.esas.tools.screens.numpad.numpad_text

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainNumpadTextBinding
import by.esas.tools.numpad.INumPadHandler

class NumpadTextFragment : AppFragment<NumpadTextVM, FMainNumpadTextBinding>() {
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
        viewModel.iconsTextSizeLive.observe(this) { size ->
//            binding.fMainNumpadText.setNumbersTextSize(20)
        }
        viewModel.iconsIsDefaultLive.observe(this) { isDefault ->
            if (isDefault)
                binding.fMainNumpadText.setNumbersDefaultStyle()
            else
                binding.fMainNumpadText.setNumbersTextStyle(R.style.NumpadTextNumbersStyle)
        }
    }
}
