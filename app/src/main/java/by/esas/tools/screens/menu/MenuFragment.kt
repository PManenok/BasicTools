package by.esas.tools.screens.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.baseui.Config.ERROR_MESSAGE_DIALOG
import by.esas.tools.databinding.FragmentMenuBinding
import by.esas.tools.dialog.SearchFilterDialog
import by.esas.tools.inputfieldview.InputFieldView
import by.esas.tools.logger.Action
import by.esas.tools.screens.MainActivity
import by.esas.tools.screens.MainActivity.Companion.CURRENT_CASE_ID
import by.esas.tools.screens.MainActivity.Companion.NEED_TO_UPDATE_CURRENT_CASE
import by.esas.tools.screens.MainActivity.Companion.NEED_TO_UPDATE_MENU
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.util_ui.defocusAndHideKeyboard
import com.google.android.material.chip.Chip

class MenuFragment : AppFragment<MenuVM, FragmentMenuBinding>() {

    companion object {
        const val MENU_UPDATE = "MENU_UPDATE"
        const val MENU_UPDATE_KEY_CLEAR = "MENU_UPDATE_KEY_CLEAR"
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

    override val fragmentDestinationId: Int = R.id.menuFragment

    override fun provideLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun provideViewModel(): MenuVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory())[MenuVM::class.java]
    }

    override fun provideRequestKeys(): List<String> {
        return listOf(ERROR_MESSAGE_DIALOG, SearchFilterDialog.FILTER_DIALOG_KEY)
    }

    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return if (requestKey == SearchFilterDialog.FILTER_DIALOG_KEY) {
            FragmentResultListener { _, result ->
                val statusesResult =
                    result.getStringArray(SearchFilterDialog.ACTION_SELECT_FILTER_STATUSES)
                val modulesResult =
                    result.getStringArray(SearchFilterDialog.ACTION_SELECT_FILTER_MODULES)
                if (statusesResult != null && modulesResult != null) {
                    viewModel.onFilterChanged(statusesResult.toList(), modulesResult.toList())
                } else {
                    viewModel.enableControls()
                }
            }
        } else
            super.provideFragmentResultListener(requestKey)
    }

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
        viewModel.casesListLive.observe(viewLifecycleOwner) { list ->
            caseAdapter.setItems(list)
        }
        viewModel.filterStatusesLive.observe(viewLifecycleOwner) { list ->
            updateFilterStatuses(list)
        }
        viewModel.filterModulesLive.observe(viewLifecycleOwner) { list ->
            updateFilterModules(list)
        }
    }

    private fun setupSearchView() {
        binding.fMenuCasesSearch.apply {
            setupEditorActionListener(object : InputFieldView.EditorActionListener {
                override fun onActionClick() {
                    by.esas.tools.util_ui.defocusAndHideKeyboard(activity)
                    viewModel.onSearchChanged(viewModel.search)
                }
            })
            setStartIconClickListener(object : InputFieldView.IconClickListener {
                override fun onIconClick() {
                    viewModel.onSearchChanged(viewModel.search)
                }
            })
            setEndIconClickListener(object : InputFieldView.IconClickListener {
                override fun onIconClick() {
                    val filterDialog = SearchFilterDialog()
                    filterDialog.setRequestKey(SearchFilterDialog.FILTER_DIALOG_KEY)
                    filterDialog.setSelectedItems(
                        binding.fMenuFilterStatuses.children.map { (it as Chip).text.toString() }.toList(),
                        binding.fMenuFilterModules.children.map { (it as Chip).text.toString() }.toList()
                    )
                    viewModel.showDialog(filterDialog)
                }
            })
        }
    }

    private fun setupCaseRecycler() {
        binding.fMenuRecycler.apply {
            adapter = caseAdapter
            layoutManager = LinearLayoutManager(this@MenuFragment.requireContext())
        }
        binding.fMenuRecycler.hasFixedSize()
    }

    private fun updateFilterStatuses(statuses: List<String>) {
        binding.fMenuFilterStatuses.removeAllViews()
        statuses.forEach {
            val chip = createFilterChip(binding.fMenuFilterStatuses.context, it)
            chip.setOnCloseIconClickListener {
                viewModel.deleteFilterStatusItem(chip.text.toString())
            }
            binding.fMenuFilterStatuses.addView(chip)
        }
    }

    private fun updateFilterModules(modules: List<String>) {
        binding.fMenuFilterModules.removeAllViews()
        modules.forEach {
            val chip = createFilterChip(binding.fMenuFilterModules.context, it)
            chip.setOnCloseIconClickListener {
                viewModel.deleteFilterModuleItem(chip.text.toString())
            }
            binding.fMenuFilterModules.addView(chip)
        }
    }

    private fun createFilterChip(context: Context, text: String): Chip {
        val chip = Chip(context)
        chip.setCloseIconResource(R.drawable.ic_close)
        chip.isCloseIconVisible = true
        chip.setChipIconTintResource(R.color.main_color_2)
        chip.setChipBackgroundColorResource(R.color.main_color_4)
        chip.setTextAppearance(R.style.TextStyle_Normal16)
        chip.textEndPadding = 0f
        chip.text = text

        return chip
    }
}
