package by.esas.tools.screens.menu

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

        setupCaseRecycler()
        viewModel.updateAdapter(viewModel.allCases)
        logger.logInfo("my onViewCreated")
        binding.fMenuCasesSearch.addTextWatcher(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(search: Editable?) {
                viewModel.onSearchChanged(search.toString())
            }
        })
    }

    override fun provideChecks(): List<Checking> {
        TODO("Not yet implemented")
    }

    override fun provideChecker(): Checker {
        TODO("Not yet implemented")
    }

    private fun setupCaseRecycler(){
        binding.fMenuRecycler.apply {
            adapter = viewModel.caseAdapter
            layoutManager = LinearLayoutManager(this@MenuFragment.requireContext())
        }
        binding.fMenuRecycler.hasFixedSize()
    }
}