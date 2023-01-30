package by.esas.tools.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.DfSearchFilterBinding
import by.esas.tools.entity.Modules
import by.esas.tools.entity.TestStatusEnum
import by.esas.tools.topbarview.ITopbarHandler

private const val SELECTED_MODULES_KEY = "SELECTED_MODULES_KEY"
private const val SELECTED_STATUSES_KEY = "SELECTED_STATUSES_KEY"

class SearchFilterDialog: BindingDialogFragment<DfSearchFilterBinding>() {

    companion object {
        const val ACTION_SELECT_FILTER_MODULES = "ACTION_SELECT_FILTER_MODULES"
        const val ACTION_SELECT_FILTER_STATUSES = "ACTION_SELECT_FILTER_STATUSES"
        const val FILTER_DIALOG_KEY = "FILTER_DIALOG_KEY"
    }

    private var moduleBoxes = listOf<Pair<CheckBox, String>>()
    private var statusBoxes = listOf<Pair<CheckBox, String>>()

    private var selectedModules = arrayListOf<String>()
    private var selectedStatuses = arrayListOf<String>()

    init {
        isCancelable = true
    }

    override fun provideLayoutId() = R.layout.df_search_filter

    override fun provideSwitchableList(): List<View?> = emptyList()

    override fun provideValidationList(): List<Checking> = emptyList()

    override fun provideVariableId() = BR.handler

    override fun styleSettings() {
        setStyle(STYLE_NO_FRAME, R.style.AppTheme)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        statusBoxes = listOf(
            Pair(binding.dfFilterStatusChecked, App.appContext.getString(R.string.case_status_checked)),
            Pair(binding.dfFilterStatusUnchecked, App.appContext.getString(R.string.case_status_unchecked)),
            Pair(binding.dfFilterStatusFailed, App.appContext.getString(R.string.case_status_failed)),
            Pair(binding.dfFilterStatusInProgress, App.appContext.getString(R.string.case_status_in_process))
        )
        moduleBoxes = listOf(
            Pair(binding.dfFilterModuleAccesscontainer, Modules.ACCESS_CONTAINER),
            Pair(binding.dfFilterModuleBasedaggerui, Modules.BASE_DAGGER_UI),
            Pair(binding.dfFilterModuleBaseui, Modules.BASE_UI),
            Pair(binding.dfFilterModuleBiometricDecryption, Modules.BIOMETRIC_DECRYPTION),
            Pair(binding.dfFilterModuleCardline, Modules.CARDLINE),
            Pair(binding.dfFilterModuleChecker, Modules.CHECKER),
            Pair(binding.dfFilterModuleCustomswitch, Modules.CUSTOMSWITCH),
            Pair(binding.dfFilterModuleDialog, Modules.DIALOG),
            Pair(binding.dfFilterModuleDimenUtil, Modules.DIMEN_UTIL),
            Pair(binding.dfFilterModuleDomain, Modules.DOMAIN),
            Pair(binding.dfFilterModuleInputfieldview, Modules.INPUTFIELD_VIEW),
            Pair(binding.dfFilterModuleListheader, Modules.LISTHEADER),
            Pair(binding.dfFilterModuleLogger, Modules.LOGGER),
            Pair(binding.dfFilterModuleNumpad, Modules.NUMPAD),
            Pair(binding.dfFilterModulePinview, Modules.PIN_VIEW),
            Pair(binding.dfFilterModuleRecycler, Modules.RECYCLER),
            Pair(binding.dfFilterModuleTimeparser, Modules.TIMEPARSER),
            Pair(binding.dfFilterModuleTopbarview, Modules.TOPBAR_VIEW),
            Pair(binding.dfFilterModuleUtil, Modules.UTIL)
        )

        setSelectedStatuses()
        setSelectedModules()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dfFilterTopbar.setupHandler(object : ITopbarHandler {
            override fun onActionClick() {
                resultBundle.putString(Config.DIALOG_USER_ACTION, Config.DISMISS_DIALOG)
                resultBundle.putStringArray(ACTION_SELECT_FILTER_STATUSES, emptyArray())
                resultBundle.putStringArray(ACTION_SELECT_FILTER_MODULES, emptyArray())

                this@SearchFilterDialog.dismiss()
            }

            override fun onNavigationClick() {
                this@SearchFilterDialog.dismiss()
            }
        })

        binding.dfFilterShowResultBtn.setOnClickListener {
            resultBundle.putString(Config.DIALOG_USER_ACTION, Config.DISMISS_DIALOG)
            resultBundle.putStringArray(ACTION_SELECT_FILTER_STATUSES, getSelectedStatuses().toTypedArray())
            resultBundle.putStringArray(ACTION_SELECT_FILTER_MODULES, getSelectedModules().toTypedArray())

            this@SearchFilterDialog.dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList(SELECTED_STATUSES_KEY, selectedStatuses)
        outState.putStringArrayList(SELECTED_MODULES_KEY, selectedModules)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        selectedStatuses = savedInstanceState?.getStringArrayList(SELECTED_STATUSES_KEY) ?: arrayListOf()
        selectedModules = savedInstanceState?.getStringArrayList(SELECTED_MODULES_KEY) ?: arrayListOf()
        setSelectedStatuses()
        setSelectedModules()
    }

    fun setSelectedItems(statuses: List<String>, modules: List<String>) {
        selectedStatuses.clear()
        selectedStatuses.addAll(statuses)

        selectedModules.clear()
        selectedModules.addAll(modules)
    }

    private fun setSelectedStatuses() {
        selectedStatuses.forEach {
            when(it) {
                App.appContext.getString(R.string.case_status_checked) -> binding.dfFilterStatusChecked.isChecked = true
                App.appContext.getString(R.string.case_status_in_process) -> binding.dfFilterStatusInProgress.isChecked = true
                App.appContext.getString(R.string.case_status_failed) -> binding.dfFilterStatusFailed.isChecked = true
                App.appContext.getString(R.string.case_status_unchecked) -> binding.dfFilterStatusUnchecked.isChecked = true
            }
        }
    }

    private fun setSelectedModules() {
        selectedModules.forEach { selectedModule ->
            moduleBoxes.find { it.second == selectedModule }?.first?.isChecked = true
        }
    }

    private fun getSelectedStatuses() = statusBoxes.filter { it.first.isChecked }.map { it.second }

    private fun getSelectedModules() = moduleBoxes.filter { it.first.isChecked }.map { it.second }
}
