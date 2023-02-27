package by.esas.tools.screens.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FragmentMenuBinding
import by.esas.tools.inputfieldview.InputFieldView
import by.esas.tools.logger.Action
import by.esas.tools.screens.MainActivity
import by.esas.tools.screens.MainActivity.Companion.CURRENT_CASE_ID
import by.esas.tools.screens.MainActivity.Companion.NEED_TO_UPDATE_CURRENT_CASE
import by.esas.tools.screens.MainActivity.Companion.NEED_TO_UPDATE_MENU
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.util.defocusAndHideKeyboard

class MenuFragment : AppFragment<MenuVM, FragmentMenuBinding>() {

    companion object {
        const val MENU_UPDATE = "MENU_UPDATE"
        const val MENU_UPDATE_KEY_CLEAR = "MENU_UPDATE_KEY_CLEAR"
    }

    override val fragmentDestinationId: Int = R.id.menuFragment

    override fun provideLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun provideViewModel(): MenuVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory())[MenuVM::class.java]
    }

    private val caseAdapter = CaseAdapter(
        onClick = { item ->
            logger.logInfo("${item.name} clicked")
            if (activity is MainActivity) {
                val bundle = Bundle()
                bundle.putInt(CURRENT_CASE_ID, item.id)
                (activity as MainActivity).handleAction(Action(NEED_TO_UPDATE_CURRENT_CASE, bundle))
            }
            navController?.navigate(item.id)
        }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().supportFragmentManager.setFragmentResultListener(MENU_UPDATE, viewLifecycleOwner) { _, bundle ->
            val actionName = bundle.getString(MENU_UPDATE)
            if (actionName == MENU_UPDATE_KEY_CLEAR)
                viewModel.clearCaseStatuses()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCaseRecycler()
        setupSearchView()
        val needUpdate = activity?.intent?.getBooleanExtra(NEED_TO_UPDATE_MENU, false) ?: false
        if (needUpdate) {
            viewModel.setCases()
            activity?.intent?.removeExtra(NEED_TO_UPDATE_MENU)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.casesListLive.observe(viewLifecycleOwner){ list ->
            caseAdapter.setItems(list)
        }
    }

    private fun setupSearchView() {
        binding.fMenuCasesSearch.apply {
            setupEditorActionListener(object : InputFieldView.EditorActionListener {
                override fun onActionClick() {
                    defocusAndHideKeyboard(activity)
                    viewModel.onSearchChanged(viewModel.search)
                }
            })
            setStartIconClickListener(object : InputFieldView.IconClickListener{
                override fun onIconClick() {
                    viewModel.onSearchChanged(viewModel.search)
                }
            })
            setEndIconClickListener(object : InputFieldView.IconClickListener{
                override fun onIconClick() {
                    viewModel.clearSearch()
                }
            })
        }
    }

    private fun setupCaseRecycler(){
        binding.fMenuRecycler.apply {
            adapter = caseAdapter
            layoutManager = LinearLayoutManager(this@MenuFragment.requireContext())
        }
        binding.fMenuRecycler.hasFixedSize()
    }
}