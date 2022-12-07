package by.esas.tools.screens.inputfield_view.start_icon

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainInputfieldviewStartIconBinding
import by.esas.tools.inputfieldview.InputFieldView

class InputfieldViewStartIconFragment: AppFragment<InputfieldViewStartIconVM, FMainInputfieldviewStartIconBinding>() {
    override val fragmentDestinationId = R.id.inputfieldViewStartIconFragment

    override fun provideLayoutId() = R.layout.f_main_inputfieldview_start_icon

    override fun provideViewModel(): InputfieldViewStartIconVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(InputfieldViewStartIconVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fInputfieldviewStartCase2Input.setStartIconCheckListener(object :
            InputFieldView.IconCheckedListener {
            override fun onCheckChanged(isChanged: Boolean) {
                val message =
                    if (isChanged) resources.getString(R.string.inputfieldview_start_icon_is_checked)
                    else resources.getString(R.string.inputfieldview_start_icon_is_unchecked)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
        binding.fInputfieldviewStartCase3Input.setStartIconClickListener(object :
            InputFieldView.IconClickListener {
            override fun onIconClick() {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.inputfieldview_start_icon_click),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        binding.fInputfieldviewStartSetErrorButton.setOnClickListener {
            setErrors()
        }
        binding.fInputfieldviewStartResetErrorButton.setOnClickListener {
            resetErrors()
        }
        binding.fInputfieldviewStartChangeStyleButton.setOnClickListener {
            changeStyles()
        }
    }

    private fun changeStyles() {
        binding.fInputfieldviewStartCase2Input.apply {
            setStartIconTintRes(R.color.inputfieldview_selector_icon_color)
        }
        binding.fInputfieldviewStartCase3Input.apply {
            setupIconsSize(by.esas.tools.inputfieldview.dpToPx(36).toInt())
            setStartIconTintRes(R.color.inputfieldview_selector_icon_color)
            setStartIconDrawable(R.drawable.ic_search)
        }
        binding.fInputfieldviewStartCase5Input.apply {
            setStartIconTintRes(R.color.inputfieldview_selector_icon_color)
        }
    }

    private fun setErrors() {
        binding.fInputfieldviewStartCase1Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewStartCase2Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewStartCase3Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewStartCase4Input.setError(resources.getString(R.string.error_text))
        binding.fInputfieldviewStartCase5Input.setError(resources.getString(R.string.error_text))
    }

    private fun resetErrors() {
        binding.fInputfieldviewStartCase1Input.setError(null)
        binding.fInputfieldviewStartCase2Input.setError(null)
        binding.fInputfieldviewStartCase3Input.setError(null)
        binding.fInputfieldviewStartCase4Input.setError(null)
        binding.fInputfieldviewStartCase5Input.setError(null)
    }
}