package by.esas.tools.screens.baseui.navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainBaseuiNavigationSecondBinding

class BaseUINavigationSecondFragment: AppFragment<BaseUINavigationSecondVM, FMainBaseuiNavigationSecondBinding>() {
    override val fragmentDestinationId = R.id.baseuiNavigationSecondFragment

    override fun provideLayoutId() = R.layout.f_main_baseui_navigation_second

    override fun provideViewModel(): BaseUINavigationSecondVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(BaseUINavigationSecondVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
    }

    private fun getArgs() {
        binding.fBaseuiNavigationSecondArgs.text = arguments?.get("date").toString()
    }
}