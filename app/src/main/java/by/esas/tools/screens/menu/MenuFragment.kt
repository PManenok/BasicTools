package by.esas.tools.screens.menu

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FragmentMenuBinding
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.entity.Modules
import by.esas.tools.inputfieldview.InputFieldView
import by.esas.tools.util.defocusAndHideKeyboard

class MenuFragment : AppFragment<MenuVM, FragmentMenuBinding>() {

    override val fragmentDestinationId: Int = R.id.menuFragment

    override fun provideLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun provideViewModel(): MenuVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(MenuVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCases()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCaseRecycler()
        setupSearchView()
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
            adapter = viewModel.caseAdapter
            layoutManager = LinearLayoutManager(this@MenuFragment.requireContext())
        }
        binding.fMenuRecycler.hasFixedSize()
    }

    private fun setCases() {
        viewModel.allCases.clear()
        addCaseItem(
            R.string.case_label_logger,
            listOf(Modules.LOGGER),
            MenuFragmentDirections.actionMenuFragmentToLoggerFragment()
        )
        addCaseItem(
            R.string.case_label_timeparser,
            listOf(Modules.TIMEPARSER),
            MenuFragmentDirections.actionMenuFragmentToTimeParserFragment()
        )
        addCaseItem(
            R.string.case_label_biometric_decryption,
            listOf(Modules.BIOMETRIC_DECRYPTION),
            MenuFragmentDirections.actionMenuFragmentToBiometricDecryptionFragment()
        )
        addCaseItem(
            R.string.case_label_domain,
            listOf(Modules.DOMAIN),
            MenuFragmentDirections.actionMenuFragmentToDomainCaseFragment()
        )
        addCaseItem(
            R.string.case_label_baseui_theme,
            listOf(Modules.BASE_UI),
            MenuFragmentDirections.actionMenuFragmentToBaseuiThemeFragment()
        )
        addCaseItem(
            R.string.case_label_baseui,
            listOf(Modules.BASE_UI),
            MenuFragmentDirections.actionMenuFragmentToBaseuiFunctionalityFragment()
        )
        addCaseItem(
            R.string.case_label_baseui_navigation,
            listOf(Modules.BASE_UI),
            MenuFragmentDirections.actionMenuFragmentToBaseuiNavigationFragment()
        )
        addCaseItem(
            R.string.case_label_bottom_dialog,
            listOf(Modules.DIALOG, Modules.INPUTFIELD_VIEW),
            MenuFragmentDirections.actionMenuFragmentToBottomDialogFragment()
        )
        addCaseItem(
            R.string.case_label_dynamic_message_dialog,
            listOf(Modules.DIALOG),
            MenuFragmentDirections.actionMenuFragmentToDynamicMessageDialogFragment()
        )
        addCaseItem(
            R.string.case_label_inputfield_view,
            listOf(Modules.INPUTFIELD_VIEW),
            MenuFragmentDirections.actionMenuFragmentToInputfieldViewFragment()
        )
        addCaseItem(
            R.string.case_label_inputfield_view_start_icon,
            listOf(Modules.INPUTFIELD_VIEW),
            MenuFragmentDirections.actionMenuFragmentToInputfieldViewStartIconFragment()
        )
        addCaseItem(
            R.string.case_label_inputfield_view_end_icon,
            listOf(Modules.INPUTFIELD_VIEW),
            MenuFragmentDirections.actionMenuFragmentToInputfieldViewEndIconFragment()
        )
        addCaseItem(
            R.string.case_label_checker,
            listOf(Modules.CHECKER, Modules.INPUTFIELD_VIEW),
            MenuFragmentDirections.actionMenuFragmentToCheckerFragment()
        )
        addCaseItem(
            R.string.case_label_pin_view,
            listOf(Modules.PIN_VIEW),
            MenuFragmentDirections.actionMenuFragmentToPinViewFragment()
        )
        addCaseItem(
            R.string.case_label_dynamic_pin_view,
            listOf(Modules.PIN_VIEW),
            MenuFragmentDirections.actionMenuFragmentToDynamicPinViewFragment()
        )
        addCaseItem(
            R.string.case_label_saved_state,
            listOf(Modules.BASE_DAGGER_UI, Modules.BASE_UI),
            MenuFragmentDirections.actionMenuFragmentToSavedStateFragment()
        )
        addCaseItem(
            R.string.case_label_numpad_image,
            listOf(Modules.NUMPAD),
            MenuFragmentDirections.actionMenuFragmentToNumpadImageFragment()
        )
        addCaseItem(
            R.string.case_label_numpad_text,
            listOf(Modules.NUMPAD),
            MenuFragmentDirections.actionMenuFragmentToNumpadTextFragment()
        )
        addCaseItem(
            R.string.case_label_util_keyboard,
            listOf(Modules.UTIL),
            MenuFragmentDirections.actionMenuFragmentToUtilKeyboardFragment()
        )
        addCaseItem(
            R.string.case_label_utils,
            listOf(Modules.UTIL),
            MenuFragmentDirections.actionMenuFragmentToUtilUtilsFragment()
        )
        addCaseItem(
            R.string.case_label_switch_manager,
            listOf(Modules.UTIL, Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToUtilSwitchManagerFragment()
        )
        addCaseItem(
            R.string.case_label_custom_switch,
            listOf(Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToCustomSwitchFragment()
        )
        addCaseItem(
            R.string.case_label_dynamic_custom_switch,
            listOf(Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToCustomSwitchProgramFragment()
        )
        addCaseItem(
            R.string.case_label_listheader,
            listOf(Modules.LISTHEADER, Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToListheaderFragment()
        )
        addCaseItem(
            R.string.case_label_dynamic_listheader,
            listOf(Modules.LISTHEADER),
            MenuFragmentDirections.actionMenuFragmentToDynamicListheaderFragment()
        )
        addCaseItem(
            R.string.case_label_topbar_view,
            listOf(Modules.TOPBAR_VIEW),
            MenuFragmentDirections.actionMenuFragmentToTopbarFragment()
        )
        addCaseItem(
            R.string.case_label_cardline,
            listOf(Modules.CARDLINE),
            MenuFragmentDirections.actionMenuFragmentToCardlineFragment()
        )
        addCaseItem(
            R.string.case_label_dynamic_cardline,
            listOf(Modules.CARDLINE),
            MenuFragmentDirections.actionMenuFragmentToDynamicCardlineFragment()
        )
        addCaseItem(
            R.string.case_label_recycler_base,
            listOf(Modules.RECYCLER),
            MenuFragmentDirections.actionMenuFragmentToRecyclerFragment()
        )
        addCaseItem(
            R.string.case_label_recycler_sticky,
            listOf(Modules.RECYCLER),
            MenuFragmentDirections.actionMenuFragmentToStickyCaseFragment()
        )
        addCaseItem(
            R.string.case_label_recycler_simple,
            listOf(Modules.RECYCLER),
            MenuFragmentDirections.actionMenuFragmentToSimpleRecyclerFragment()
        )
        addCaseItem(
            R.string.case_label_recycler_custom,
            listOf(Modules.RECYCLER),
            MenuFragmentDirections.actionMenuFragmentToCustomRecyclerFragment()
        )
        viewModel.updateAdapter(viewModel.allCases)
    }

    private fun addCaseItem(
        name: Int,
        modulesList: List<String>,
        direction: NavDirections? = null
    ) {
        val caseName = resources.getString(name)
        viewModel.allCases.add(CaseItemInfo(viewModel.allCases.size, caseName, modulesList, direction))
    }
}