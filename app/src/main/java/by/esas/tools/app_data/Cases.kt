package by.esas.tools.app_data

import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.entity.CaseInfo
import by.esas.tools.entity.Modules

object Cases {

    private val cases = arrayListOf<CaseInfo>()

    fun getAll(): List<CaseInfo> {
        cases.clear()
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
            R.id.domainCaseFragment,
            R.string.case_label_domain,
            listOf(Modules.DOMAIN)
        )
        addCaseItem(
            R.id.baseuiThemeFragment,
            R.string.case_label_baseui_theme,
            listOf(Modules.BASE_UI)
        )
        addCaseItem(
            R.id.baseuiFunctionalityFragment,
            R.string.case_label_baseui,
            listOf(Modules.BASE_UI)
        )
        addCaseItem(
            R.id.baseuiNavigationFragment,
            R.string.case_label_baseui_navigation,
            listOf(Modules.BASE_UI)
        )
        addCaseItem(
            R.id.bottomDialogFragment,
            R.string.case_label_bottom_dialog,
            listOf(Modules.DIALOG, Modules.INPUTFIELD_VIEW)
        )
        addCaseItem(
            R.id.dynamicMessageDialogFragment,
            R.string.case_label_dynamic_message_dialog,
            listOf(Modules.DIALOG)
        )
        addCaseItem(
            R.id.inputfieldViewFragment,
            R.string.case_label_inputfield_view,
            listOf(Modules.INPUTFIELD_VIEW)
        )
        addCaseItem(
            R.id.inputfieldViewStartIconFragment,
            R.string.case_label_inputfield_view_start_icon,
            listOf(Modules.INPUTFIELD_VIEW)
        )
        addCaseItem(
            R.id.inputfieldViewEndIconFragment,
            R.string.case_label_inputfield_view_end_icon,
            listOf(Modules.INPUTFIELD_VIEW)
        )
        addCaseItem(
            R.id.checkerFragment,
            R.string.case_label_checker,
            listOf(Modules.CHECKER, Modules.INPUTFIELD_VIEW)
        )
        addCaseItem(
            R.id.pinViewFragment,
            R.string.case_label_pin_view,
            listOf(Modules.PIN_VIEW)
        )
        addCaseItem(
            R.id.dynamicPinViewFragment,
            R.string.case_label_dynamic_pin_view,
            listOf(Modules.PIN_VIEW)
        )
        addCaseItem(
            R.id.savedStateFragment,
            R.string.case_label_saved_state,
            listOf(Modules.BASE_DAGGER_UI, Modules.BASE_UI)
        )
        addCaseItem(
            R.id.numpadImageFragment,
            R.string.case_label_numpad_image,
            listOf(Modules.NUMPAD)
        )
        addCaseItem(
            R.id.numpadTextFragment,
            R.string.case_label_numpad_text,
            listOf(Modules.NUMPAD)
        )
        addCaseItem(
            R.id.utilKeyboardFragment,
            R.string.case_label_util_keyboard,
            listOf(Modules.UTIL)
        )
        addCaseItem(
            R.id.utilUtilsFragment,
            R.string.case_label_utils,
            listOf(Modules.UTIL)
        )
        addCaseItem(
            R.id.utilSwitchManagerFragment,
            R.string.case_label_switch_manager,
            listOf(Modules.UTIL, Modules.CUSTOMSWITCH)
        )
        addCaseItem(
            R.id.customSwitchFragment,
            R.string.case_label_custom_switch,
            listOf(Modules.CUSTOMSWITCH)
        )
        addCaseItem(
            R.id.customSwitchProgramFragment,
            R.string.case_label_dynamic_custom_switch,
            listOf(Modules.CUSTOMSWITCH),
        )
        addCaseItem(
            R.id.listheaderFragment,
            R.string.case_label_listheader,
            listOf(Modules.LISTHEADER, Modules.CUSTOMSWITCH)
        )
        addCaseItem(
            R.id.dynamicListheaderFragment,
            R.string.case_label_dynamic_listheader,
            listOf(Modules.LISTHEADER)
        )
        addCaseItem(
            R.id.topbarFragment,
            R.string.case_label_topbar_view,
            listOf(Modules.TOPBAR_VIEW)
        )
        addCaseItem(
            R.id.cardlineFragment,
            R.string.case_label_cardline,
            listOf(Modules.CARDLINE)
        )
        addCaseItem(
            R.id.dynamicCardlineFragment,
            R.string.case_label_dynamic_cardline,
            listOf(Modules.CARDLINE)
        )
        addCaseItem(
            R.id.recyclerFragment,
            R.string.case_label_recycler_base,
            listOf(Modules.RECYCLER)
        )
        addCaseItem(
            R.id.stickyCaseFragment,
            R.string.case_label_recycler_sticky,
            listOf(Modules.RECYCLER)
        )
        addCaseItem(
            R.id.simpleRecyclerFragment,
            R.string.case_label_recycler_simple,
            listOf(Modules.RECYCLER)
        )
        addCaseItem(
            R.id.customRecyclerFragment,
            R.string.case_label_recycler_custom,
            listOf(Modules.RECYCLER)
        )
        addCaseItem(
            R.id.accessContainerFragment,
            R.string.case_label_access_container,
            listOf(
                Modules.ACCESS_CONTAINER,
                Modules.LISTHEADER,
                Modules.INPUTFIELD_VIEW,
                Modules.NUMPAD,
                Modules.DIALOG
            )
        )

        return cases
    }

    //REMEMBER to add some static mapping to set static ID's for cases
    private fun addCaseItem(
        id: Int,
        name: Int,
        modulesList: List<String>
    ) {
        val caseName = App.appContext.getString(name)
        cases.add(CaseInfo(id, caseName, modulesList))
    }
}