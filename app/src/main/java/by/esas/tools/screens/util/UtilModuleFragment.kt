package by.esas.tools.screens.util

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainUtilModuleBinding
import by.esas.tools.util.focusAndShowKeyboard
import by.esas.tools.util.hideKeyboard
import by.esas.tools.util.showKeyboard

class UtilModuleFragment : AppFragment<UtilModuleVM, FMainUtilModuleBinding>() {
    override val fragmentDestinationId = R.id.utilModuleFragment
    override fun provideLayoutId() = R.layout.f_main_util_module

    override fun provideViewModel(): UtilModuleVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(UtilModuleVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println(binding.editTextView.hasFocus())

        focusAndShowKeyboard(activity, binding.editTextView)
        println("rrrrrrrrrrrrrrr")
        println(binding.editTextView.isFocused)
        println(binding.editTextView.hasFocus())
        binding.textView.setOnClickListener {
            by.esas.tools.util.hideKeyboard(activity)
            println(binding.editTextView.hasFocus())

        }

    }

}