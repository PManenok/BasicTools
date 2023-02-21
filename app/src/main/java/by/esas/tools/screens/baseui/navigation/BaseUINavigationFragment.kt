package by.esas.tools.screens.baseui.navigation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.FMainBaseuiNavigationBinding
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking

class BaseUINavigationFragment: AppFragment<BaseUINavigationVM, FMainBaseuiNavigationBinding>() {

    companion object {
        const val ARGUMENTS_DATA = "ARGUMENTS_DATA"
    }

    override val fragmentDestinationId = R.id.baseuiNavigationFragment

    override fun provideLayoutId() = R.layout.f_main_baseui_navigation

    override fun provideViewModel(): BaseUINavigationVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(BaseUINavigationVM::class.java)
    }

    override fun provideChecks(): List<Checking> {
        return listOf(FieldChecking(binding.fBaseeuiNavigationInputField, true))
    }

    override fun provideChecker(): Checker {
        return AppChecker().setMode(false).setListener(object : Checker.CheckListener {
            override fun onFailed() {
                viewModel.enableControls()
            }

            override fun onSuccess() {
                viewModel.enableControls()
                viewModel.navigateToSecondFragment(binding.fBaseeuiNavigationInputField.getText())
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()

        binding.fBaseeuiNavigationButtonWithoutArgs.setOnClickListener {
            viewModel.navigateToSecondFragment()
        }
        binding.fBaseeuiNavigationButtonWithArgs.setOnClickListener {
            provideChecker().validate(provideChecks())
        }
    }

    private fun getArgs() {
        val data = activity?.intent?.getStringExtra(ARGUMENTS_DATA)
        if (data != null) {
            Toast.makeText(requireContext(), "Get data: $data", Toast.LENGTH_LONG).show()
            activity?.intent?.removeExtra(ARGUMENTS_DATA)
        }
    }
}
