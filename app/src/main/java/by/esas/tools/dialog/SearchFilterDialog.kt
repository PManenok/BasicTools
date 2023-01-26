package by.esas.tools.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import by.esas.tools.R
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.DfSearchFilterBinding
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

    private var selectedModules = arrayListOf<Int>()
    private var selectedStatuses = arrayListOf<Int>()

    init {
        isCancelable = true
    }

    override fun provideLayoutId() = R.layout.df_search_filter

    override fun provideSwitchableList(): List<View?> = emptyList()

    override fun provideValidationList(): List<Checking> = emptyList()

    override fun provideVariableId() = BR.handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_FRAME, R.style.AppTheme)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusBoxes = listOf(
            binding.dfFilterStatusChecked, binding.dfFilterStatusUnchecked, binding.dfFilterStatusFailed, binding.dfFilterStatusInProgress
        )
        val moduleBoxes = listOf(
            binding.dfFilterModuleAccesscontainer,
            binding.dfFilterModuleBasedaggerui,
            binding.dfFilterModuleBaseui,
            binding.dfFilterModuleBiometricDecryption,
            binding.dfFilterModuleCardline,
            binding.dfFilterModuleChecker,
            binding.dfFilterModuleCustomswitch,
            binding.dfFilterModuleDialog,
            binding.dfFilterModuleDimenUtil,
            binding.dfFilterModuleDomain,
            binding.dfFilterModuleInputfieldview,
            binding.dfFilterModuleListheader,
            binding.dfFilterModuleLogger,
            binding.dfFilterModuleNumpad,
            binding.dfFilterModulePinview,
            binding.dfFilterModuleRecycler,
            binding.dfFilterModuleTimeparser,
            binding.dfFilterModuleTopbarview,
            binding.dfFilterModuleUtil
        )

        binding.dfFilterTopbar.setupHandler(object : ITopbarHandler {
            override fun onActionClick() {
                resultBundle.putString(Config.DIALOG_USER_ACTION, Config.DISMISS_DIALOG)
                resultBundle.putString(ACTION_SELECT_FILTER_STATUSES, "")
                resultBundle.putString(ACTION_SELECT_FILTER_MODULES, "")

                this@SearchFilterDialog.dismiss()
            }

            override fun onNavigationClick() {
                this@SearchFilterDialog.dismiss()
            }
        })

        statusBoxes.forEach {
            it.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    selectedStatuses.add(buttonView.id)
                else
                    selectedStatuses.remove(buttonView.id)
            }
        }

        moduleBoxes.forEach {
            it.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    selectedModules.add(buttonView.id)
                else
                    selectedModules.remove(buttonView.id)
            }
        }

        binding.dfFilterShowResultBtn.setOnClickListener {
            resultBundle.putString(Config.DIALOG_USER_ACTION, Config.DISMISS_DIALOG)
            resultBundle.putStringArray(ACTION_SELECT_FILTER_STATUSES, getStatusList(selectedStatuses).toTypedArray())
            resultBundle.putStringArray(ACTION_SELECT_FILTER_MODULES, getModuleList(selectedModules).toTypedArray())

            this@SearchFilterDialog.dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putIntegerArrayList(SELECTED_STATUSES_KEY, selectedStatuses)
        outState.putIntegerArrayList(SELECTED_MODULES_KEY, selectedModules)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        selectedStatuses = savedInstanceState?.getIntegerArrayList(SELECTED_STATUSES_KEY) ?: arrayListOf()
        selectedModules = savedInstanceState?.getIntegerArrayList(SELECTED_MODULES_KEY) ?: arrayListOf()
    }

    private fun getStatusList(boxesList: List<Int>): List<String> {
        val modules = boxesList.map { when(it) {
            binding.dfFilterStatusChecked.id -> TestStatusEnum.CHECKED.name
            binding.dfFilterStatusInProgress.id -> TestStatusEnum.IN_PROCESS.name
            binding.dfFilterStatusFailed.id -> TestStatusEnum.FAILED.name
            else -> TestStatusEnum.UNCHECKED.name
        } }

        return modules
    }

    private fun getModuleList(boxesList: List<Int>): List<String> {
        val boxes = boxesList.map { view?.findViewById<CheckBox>(it)?.text }
        val searchList = arrayListOf<String>()
        boxes.forEach { searchList.add(it.toString()) }

        return searchList
    }
}