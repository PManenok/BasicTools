package by.esas.tools.screens.inputfield_view.input_field

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainInputfieldviewBinding
import by.esas.tools.inputfieldview.InputFieldView

class InputfieldViewFragment: AppFragment<InputfieldViewVM, FMainInputfieldviewBinding>() {
    override val fragmentDestinationId = R.id.inputfieldViewFragment

    override fun provideLayoutId() = R.layout.f_main_inputfieldview

    override fun provideViewModel(): InputfieldViewVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(InputfieldViewVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fInputfieldviewCase2.setStartIconClickListener(object : InputFieldView.IconClickListener {
            override fun onIconClick() {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.inputfieldview_start_icon_click),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.fInputfieldviewSetErrorButton.setOnClickListener {
            setErrors()
        }
        binding.fInputfieldviewResetErrorButton.setOnClickListener {
            resetErrors()
        }
        binding.fInputfieldviewChangeStyleButton.setOnClickListener {
            changeStyle()
        }
    }

    private fun setErrors() {
        binding.fInputfieldviewCase1.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewCase2.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewCase3.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewCase4.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewCase5.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewCase6.setError(resources.getString(R.string.error_text))
    }

    private fun resetErrors() {
        binding.fInputfieldviewCase1.setError(null)
        binding.fInputfieldviewCase2.setError(null)
        binding.fInputfieldviewCase3.setError(null)
        binding.fInputfieldviewCase4.setError(null)
        binding.fInputfieldviewCase5.setError(null)
        binding.fInputfieldviewCase6.setError(null)
    }

    private fun changeStyle() {
        binding.fInputfieldviewCase2.apply {
            setInputLabel(R.string.inputfieldview_new_label)
            setLabelStyle(R.style.CustomSwitcherTextStyleBold)
            setStartIconDrawable(R.drawable.ic_search)
            setStartIconTintRes(R.color.yellow)
            setEndIconDrawable(R.drawable.ic_close)
            setEndIconTintRes(R.color.yellow)
            setHelpStyle(R.style.CustomSwitcherInfoTextStyle)
            setupBoxBackgroundColorRes(R.color.orange_light)
        }
        binding.fInputfieldviewCase3.apply {
            setupStrokeColorRes(R.color.yellow)
            setStartIconTintRes(R.color.yellow)
            setEndIconDrawable(R.drawable.ic_search)
            setEndIconTintRes(R.color.yellow)
            setInputStyle(R.style.CustomSwitcherTextStyleNormal)
        }
        binding.fInputfieldviewCase4.apply {
            setStartIconTintRes(R.color.purple)
            setEndText(resources.getString(R.string.action))
            setEndTextStyle(R.style.CustomSwitcherTitleTextStyle)
        }
        binding.fInputfieldviewCase5.apply {
            setInputLabel(R.string.inputfieldview_new_label)
            setupIconsSize(by.esas.tools.inputfieldview.dpToPx(36).toInt())
            setupStrokeColorRes(R.color.purple)
        }
        binding.fInputfieldviewCase6.apply {
            setupStrokeErrorColorRes(R.color.orange)
        }
    }
}
