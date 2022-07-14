package by.esas.tools.menu

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.FragmentMenuBinding
import by.esas.tools.simple.AppFragment
import dagger.android.support.AndroidSupportInjection

class MenuFragment : AppFragment<MenuVM, FragmentMenuBinding>() {
    override val fragmentDestinationId: Int = R.id.menuFragment

    override fun provideLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun provideViewModel(): MenuVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(MenuVM::class.java)
    }


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun provideChecks(): List<Checking> {
        TODO("Not yet implemented")
    }

    override fun provideChecker(): Checker {
        TODO("Not yet implemented")
    }

}