package by.esas.tools.screens.menu

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.FragmentMenuBinding
import by.esas.tools.inputfieldview.InputFieldView
import by.esas.tools.simple.AppFragment
import by.esas.tools.util.defocusAndHideKeyboard
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
        setupSearchView()
        viewModel.updateAdapter(viewModel.allCases)
    }

    override fun provideChecks(): List<Checking> {
        TODO("Not yet implemented")
    }

    override fun provideChecker(): Checker {
        TODO("Not yet implemented")
    }

    private fun setupSearchView() {
        binding.fMenuCasesSearch.apply {
            inputText?.imeOptions = EditorInfo.IME_ACTION_DONE
            inputText?.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    defocusAndHideKeyboard(activity)
                    viewModel.onSearchChanged(viewModel.search)
                    true
                } else {
                    false
                }
            }
            startIconView?.setOnClickListener {
                viewModel.onSearchChanged(viewModel.search)
            }
        }
    }

    private fun setupCaseRecycler(){
        binding.fMenuRecycler.apply {
            adapter = viewModel.caseAdapter
            layoutManager = LinearLayoutManager(this@MenuFragment.requireContext())
        }
        binding.fMenuRecycler.hasFixedSize()
    }
}