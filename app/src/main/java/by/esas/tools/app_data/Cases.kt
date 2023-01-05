package by.esas.tools.app_data

import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.entity.CaseInfo
import by.esas.tools.entity.Modules

object Cases {
    private val cases = arrayListOf<CaseInfo>()

    init {
        addCaseItem(
            R.id.loggerFragment,
            R.string.case_label_logger,
            listOf(Modules.LOGGER)
        )
        addCaseItem(
            R.id.timeparserFragment,
            R.string.case_label_timeparser,
            listOf(Modules.TIMEPARSER)
        )
        addCaseItem(
            R.id.biometricDecryptionFragment,
            R.string.case_label_biometric_decryption,
            listOf(Modules.BIOMETRIC_DECRYPTION)
        )
        addCaseItem(
            -1,
            R.string.case_label_domain,
            listOf(Modules.DOMAIN)
        )
        addCaseItem(
            -1,
            R.string.case_label_baseui_theme,
            listOf(Modules.BASE_UI)
        )
        addCaseItem(
            -1,
            R.string.case_label_baseui,
            listOf(Modules.BASE_UI)
        )
        addCaseItem(
            R.id.baseuiNavigationFragment,
            R.string.case_label_baseui_navigation,
            listOf(Modules.BASE_UI)
        )
//        addCaseItem(
//            -1,
//            R.string.case_label_bottom_dialog,
//            listOf(Modules.DIALOG, Modules.INPUTFIELD_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToBottomDialogFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_dynamic_message_dialog,
//            listOf(Modules.DIALOG),
//            MenuFragmentDirections.actionMenuFragmentToDynamicMessageDialogFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_inputfield_view,
//            listOf(Modules.INPUTFIELD_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToInputfieldViewFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_inputfield_view_start_icon,
//            listOf(Modules.INPUTFIELD_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToInputfieldViewStartIconFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_inputfield_view_end_icon,
//            listOf(Modules.INPUTFIELD_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToInputfieldViewEndIconFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_checker,
//            listOf(Modules.CHECKER, Modules.INPUTFIELD_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToCheckerFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_pin_view,
//            listOf(Modules.PIN_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToPinViewFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_dynamic_pin_view,
//            listOf(Modules.PIN_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToDynamicPinViewFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_saved_state,
//            listOf(Modules.BASE_DAGGER_UI, Modules.BASE_UI),
//            MenuFragmentDirections.actionMenuFragmentToSavedStateFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_numpad_image,
//            listOf(Modules.NUMPAD),
//            MenuFragmentDirections.actionMenuFragmentToNumpadImageFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_numpad_text,
//            listOf(Modules.NUMPAD),
//            MenuFragmentDirections.actionMenuFragmentToNumpadTextFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_util_keyboard,
//            listOf(Modules.UTIL),
//            MenuFragmentDirections.actionMenuFragmentToUtilKeyboardFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_utils,
//            listOf(Modules.UTIL),
//            MenuFragmentDirections.actionMenuFragmentToUtilUtilsFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_switch_manager,
//            listOf(Modules.UTIL, Modules.CUSTOMSWITCH),
//            MenuFragmentDirections.actionMenuFragmentToUtilSwitchManagerFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_custom_switch,
//            listOf(Modules.CUSTOMSWITCH),
//            MenuFragmentDirections.actionMenuFragmentToCustomSwitchFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_dynamic_custom_switch,
//            listOf(Modules.CUSTOMSWITCH),
//            MenuFragmentDirections.actionMenuFragmentToCustomSwitchProgramFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_listheader,
//            listOf(Modules.LISTHEADER, Modules.CUSTOMSWITCH),
//            MenuFragmentDirections.actionMenuFragmentToListheaderFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_dynamic_listheader,
//            listOf(Modules.LISTHEADER),
//            MenuFragmentDirections.actionMenuFragmentToDynamicListheaderFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_topbar_view,
//            listOf(Modules.TOPBAR_VIEW),
//            MenuFragmentDirections.actionMenuFragmentToTopbarFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_cardline,
//            listOf(Modules.CARDLINE),
//            MenuFragmentDirections.actionMenuFragmentToCardlineFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_dynamic_cardline,
//            listOf(Modules.CARDLINE),
//            MenuFragmentDirections.actionMenuFragmentToDynamicCardlineFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_recycler_base,
//            listOf(Modules.RECYCLER),
//            MenuFragmentDirections.actionMenuFragmentToRecyclerFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_recycler_sticky,
//            listOf(Modules.RECYCLER),
//            MenuFragmentDirections.actionMenuFragmentToStickyCaseFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_recycler_simple,
//            listOf(Modules.RECYCLER),
//            MenuFragmentDirections.actionMenuFragmentToSimpleRecyclerFragment()
//        )
//        addCaseItem(
//            -1,
//            R.string.case_label_recycler_custom,
//            listOf(Modules.RECYCLER),
//            MenuFragmentDirections.actionMenuFragmentToCustomRecyclerFragment()
//        )
    }

    fun getAll() = cases.toList()

    private fun addCaseItem(
        id: Int,
        name: Int,
        modulesList: List<String>
    ) {
        val caseName = App.appContext.resources.getString(name)
        cases.add(CaseInfo(id, caseName, modulesList))
    }
}