package by.esas.tools.screens.util

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainUtilModuleBinding
import by.esas.tools.util.defocusAndHideKeyboard
import by.esas.tools.util.focusAndShowKeyboard
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

        focusAndShowKeyboard(activity, binding.fMainUtilCase1Edit)
        binding.fMainUtilCase1Button.setOnClickListener {
            defocusAndHideKeyboard(activity)
        }
        binding.fMainUtilCase2ButtonShow.setOnClickListener {
            showKeyboard(activity)
        }
        binding.fMainUtilCase2ButtonHide.setOnClickListener {
            by.esas.tools.util.hideKeyboard(activity)
        }
    }
}
