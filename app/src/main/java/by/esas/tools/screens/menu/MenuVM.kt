package by.esas.tools.screens.menu

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.app_domain.usecase.FilterCaseUseCase
import by.esas.tools.app_domain.usecase.GetCaseItemsUseCase
import by.esas.tools.app_domain.usecase.SearchCaseUseCase
import by.esas.tools.base.AppVM
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.entity.TestStatusEnum
import by.esas.tools.logger.Action
import javax.inject.Inject

class MenuVM @Inject constructor(
    private val searchCase: SearchCaseUseCase,
    private val filterCase: FilterCaseUseCase,
    private val getCaseItems: GetCaseItemsUseCase
) : AppVM() {

    var prevSearch = ""
    var search = ""
    var isEmpty = ObservableBoolean(false)
    val casesListLive = MutableLiveData<List<CaseItemInfo>>()
    val filterStatusesLive = MutableLiveData<List<String>>()
    val filterModulesLive = MutableLiveData<List<String>>()

    private val allCases: MutableList<CaseItemInfo> = mutableListOf()

    init {
        setCases()
    }

    fun setCases() {
        allCases.clear()
        getCaseItems.execute {
            onComplete {
                allCases.addAll(it)
                updateAdapter(it)
            }
            onError { handleError(error = it) }
        }
    }

    fun onSearchChanged(value: String) {
        if (prevSearch != value) {
            disableControls()
            prevSearch = value
            searchCase.caseItems = allCases
            searchCase.search = search
            searchCase.execute {
                onComplete { itemsList ->
                    updateAdapter(itemsList)
                    enableControls()
                }
                onError {
                    handleError(error = it)
                }
            }
        }
    }

    fun onFilterChanged(statuses: List<String>, modules: List<String>) {
        filterStatusesLive.value = statuses
        filterModulesLive.value = modules

        if (statuses.isEmpty() && modules.isEmpty() && allCases.size != casesListLive.value?.size)
            updateAdapter(allCases)
        else {
            filterCase.caseItems = allCases
            filterCase.statuses = statuses.map {
                when(it) {
                    App.appContext.getString(R.string.case_status_checked) -> TestStatusEnum.CHECKED
                    App.appContext.getString(R.string.case_status_in_process) -> TestStatusEnum.IN_PROCESS
                    App.appContext.getString(R.string.case_status_failed) -> TestStatusEnum.FAILED
                    else -> TestStatusEnum.UNCHECKED
                }
            }
            filterCase.modules = modules

            filterCase.execute {
                onComplete { itemsList ->
                    updateAdapter(itemsList)
                    enableControls()
                }
                onError {
                    handleError(error = it)
                }
            }
        }
    }

    fun deleteFilterModuleItem(item: String) {
        filterModulesLive.value?.toMutableList()?.let {
            it.remove(item)
            onFilterChanged(filterStatusesLive.value?.toList() ?: emptyList(), it)
        }
    }

    fun deleteFilterStatusItem(item: String) {
        filterStatusesLive.value?.toMutableList()?.let {
            it.remove(item)
            onFilterChanged(it, filterModulesLive.value ?: emptyList())
        }
    }

    private fun updateAdapter(list: List<CaseItemInfo>) {
        casesListLive.value = list
        isEmpty.set(list.isEmpty())
    }
}