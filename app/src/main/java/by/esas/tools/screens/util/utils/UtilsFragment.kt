package by.esas.tools.screens.util.utils

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainUtilUtilsBinding
import by.esas.tools.util.getCardType
import by.esas.tools.util.getOnlyNumbers
import by.esas.tools.util.luhnCheck
import by.esas.tools.util.toFormattedInput
import by.esas.tools.util.toFormattedString

class UtilsFragment : AppFragment<UtilsVM, FMainUtilUtilsBinding>() {

    override val fragmentDestinationId = R.id.utilUtilsFragment

    override fun provideLayoutId() = R.layout.f_main_util_utils

    override fun provideViewModel(): UtilsVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(UtilsVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainUtilsCase1Button.setOnClickListener {
            val data = binding.fMainUtilsCase1Edit.getText()
            if (data.isNotEmpty())
                binding.fMainUtilsCase1Answer.text = setupResult(data.toDouble().toFormattedInput())
        }

        binding.fMainUtilsCase2Button.setOnClickListener {
            val data = binding.fMainUtilsCase2Edit.getText()
            if (data.isNotEmpty())
                binding.fMainUtilsCase2Answer.text = setupResult(data.toDouble().toFormattedString())
        }

        binding.fMainUtilsCase3Button.setOnClickListener {
            val result = getOnlyNumbers(binding.fMainUtilsCase3Edit.getText())
            binding.fMainUtilsCase3Answer.text = setupResult(result)
        }

        binding.fMainUtilsCase4Button.setOnClickListener {
            val result = luhnCheck(binding.fMainUtilsCase4Edit.getText())
            binding.fMainUtilsCase4Answer.text = setupResult(result.toString())
        }

        binding.fMainUtilsCase5Button.setOnClickListener {
            val data = binding.fMainUtilsCase5Edit.getText()
            if (data.length >= 13)
                binding.fMainUtilsCase5Answer.text = setupResult(getCardType(data))
        }
    }

    private fun setupResult(result: String): String {
        return resources.getString(R.string.utils_result) + result
    }
}