package by.esas.tools.screens.baseui.navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.FMainBaseuiNavigationSecondBinding
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking

class BaseUINavigationSecondFragment: AppFragment<BaseUINavigationSecondVM, FMainBaseuiNavigationSecondBinding>() {
    override val fragmentDestinationId = R.id.baseuiNavigationSecondFragment

    override fun provideLayoutId() = R.layout.f_main_baseui_navigation_second

    override fun provideViewModel(): BaseUINavigationSecondVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(BaseUINavigationSecondVM::class.java)
    }

    override fun provideChecks(): List<Checking> {
        return listOf(
            FieldChecking(binding.fBaseuiNavigationSecondInputField, true)
        )
    }

    override fun provideChecker(): Checker {
        return AppChecker().setMode(false).setListener(object : Checker.CheckListener {
            override fun onFailed() {
                enableControls()
            }

            override fun onSuccess() {
                enableControls()
                val bundle = Bundle()
                bundle.putString(BaseUINavigationFragment.ARGUMENTS_DATA, binding.fBaseuiNavigationSecondInputField.getText())
                viewModel.popBack(0, inclusive = false, parameters = bundle)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()

        binding.fBaseuiNavigationSecondPopBackWithArgs.setOnClickListener {
            provideChecker().validate(provideChecks())
        }
        binding.fBaseuiNavigationSecondPopBack.setOnClickListener {
            viewModel.popBack(destination = 0, inclusive = false, parameters = null)
        }
        binding.fBaseuiNavigationSecondPopBackInclusive.setOnClickListener {
            viewModel.popBack(destination = R.id.baseuiNavigationFragment, inclusive = true, parameters = null)
        }
    }

    private fun getArgs() {
        binding.fBaseuiNavigationSecondArgs.text = arguments?.get("date").toString()
    }
}