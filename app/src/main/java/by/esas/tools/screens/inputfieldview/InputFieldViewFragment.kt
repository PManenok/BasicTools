package by.esas.tools.screens.inputfieldview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainInputfieldviewBinding
import by.esas.tools.inputfieldview.InputFieldView

class InputFieldViewFragment: AppFragment<InputFieldViewVM, FMainInputfieldviewBinding>() {

    override val fragmentDestinationId = R.id.inputfieldFragment

    override fun provideLayoutId() = R.layout.f_main_inputfieldview

    override fun provideViewModel(): InputFieldViewVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(InputFieldViewVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainInputFieldCase2.setStartIconClickListener(object : InputFieldView.IconClickListener {
            override fun onIconClick() {
                Toast.makeText(requireContext(), "on Start icon click", Toast.LENGTH_SHORT).show()
            }
        })

        binding.fMainInputFieldErrorButton.setOnClickListener {
            binding.fMainInputFieldCase1.setError("Error text")
            binding.fMainInputFieldCase2.setError("Error text")
            binding.fMainInputFieldCase3.setError("Error text")
            binding.fMainInputFieldCase4.setError("Error text")
            binding.fMainInputFieldCase5.setError("Error text")
            binding.fMainInputFieldCase6.setError("Error text")
        }

        binding.fMainInputFieldErrorResetButton.setOnClickListener {
            binding.fMainInputFieldCase1.setError(null)
            binding.fMainInputFieldCase2.setError(null)
            binding.fMainInputFieldCase3.setError(null)
            binding.fMainInputFieldCase4.setError(null)
            binding.fMainInputFieldCase5.setError(null)
            binding.fMainInputFieldCase6.setError(null)
        }
    }
}