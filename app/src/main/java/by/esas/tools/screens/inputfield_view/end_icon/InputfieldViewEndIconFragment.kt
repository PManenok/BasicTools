package by.esas.tools.screens.inputfield_view.end_icon

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainInputfieldviewEndIconBinding
import by.esas.tools.inputfieldview.InputFieldView

class InputfieldViewEndIconFragment :
    AppFragment<InputfieldViewEndIconVM, FMainInputfieldviewEndIconBinding>() {
    override val fragmentDestinationId = R.id.inputfieldViewEndIconFragment

    override fun provideLayoutId() = R.layout.f_main_inputfieldview_end_icon

    override fun provideViewModel(): InputfieldViewEndIconVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(
            InputfieldViewEndIconVM::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fInputfieldviewEndCase4Input.setEndIconClickListener(object :
            InputFieldView.IconClickListener {
            override fun onIconClick() {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.inputfieldview_end_icon_click),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.fInputfieldviewEndCase5Input.setEndIconClickListener(object :
            InputFieldView.IconClickListener {
            override fun onIconClick() {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.inputfieldview_end_icon_click),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.fInputfieldviewEndCase6Input.setEndIconCheckListener(object :
            InputFieldView.IconCheckedListener {
            override fun onCheckChanged(isChanged: Boolean) {
                val message =
                    if (isChanged) resources.getString(R.string.inputfieldview_end_icon_is_checked)
                    else resources.getString(R.string.inputfieldview_end_icon_is_unchecked)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.fInputfieldviewEndSetErrorButton.setOnClickListener {
            setErrors()
        }
        binding.fInputfieldviewEndResetErrorButton.setOnClickListener {
            resetErrors()
        }
        binding.fInputfieldviewEndChangeStyleButton.setOnClickListener {
            changeStyles()
        }
    }

    private fun setErrors() {
        binding.fInputfieldviewEndCase1Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewEndCase2Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewEndCase3Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewEndCase4Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewEndCase5Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewEndCase6Input.setError(resources.getString(R.string.error_text))
    }

    private fun resetErrors() {
        binding.fInputfieldviewEndCase1Input.setError(null)
        binding.fInputfieldviewEndCase2Input.setError(null)
        binding.fInputfieldviewEndCase3Input.setError(null)
        binding.fInputfieldviewEndCase4Input.setError(null)
        binding.fInputfieldviewEndCase5Input.setError(null)
        binding.fInputfieldviewEndCase6Input.setError(null)
    }

    private fun changeStyles() {
        binding.fInputfieldviewEndCase2Input.apply {
            setEndIconDrawable(R.drawable.ic_close)
            setEndIconTintRes(R.color.selector_inputfieldview_icon_color)
        }
        binding.fInputfieldviewEndCase3Input.apply {
            setupIconsSize(by.esas.tools.inputfieldview.dpToPx(36).toInt())
            setEndIconTintRes(R.color.selector_inputfieldview_icon_color)
        }
        binding.fInputfieldviewEndCase4Input.apply {
            setupIconsSize(by.esas.tools.inputfieldview.dpToPx(36).toInt())
            setEndIconDrawable(R.drawable.ic_search)
            setEndIconTintRes(R.color.selector_inputfieldview_icon_color)
        }
        binding.fInputfieldviewEndCase5Input.apply {
            setEndTextStyle(R.style.CustomSwitcherInfoTextStyle)
            setEndText(resources.getString(R.string.action))
        }
        binding.fInputfieldviewEndCase6Input.apply {
            setEndIconTintRes(R.color.selector_inputfieldview_icon_color)
        }
    }
}
