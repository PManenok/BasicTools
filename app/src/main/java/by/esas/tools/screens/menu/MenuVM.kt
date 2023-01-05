package by.esas.tools.screens.menu

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import by.esas.tools.app_domain.usecase.GetCaseItemsUseCase
import by.esas.tools.base.AppVM
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.usecase.SearchCaseUseCase
import javax.inject.Inject

class MenuVM @Inject constructor(
    private val searchCase: SearchCaseUseCase,
    private val getCaseItems: GetCaseItemsUseCase
) : AppVM() {

    var prevSearch = ""
    var search = ""
    var isEmpty = ObservableBoolean(false)
    val casesListLive = MutableLiveData<List<CaseItemInfo>>()

    private val allCases: MutableList<CaseItemInfo> = mutableListOf()

    init {
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

    fun clearSearch() {
        updateAdapter(allCases)
        prevSearch = ""
    }

    fun updateAdapter(list: List<CaseItemInfo>) {
        casesListLive.value = list
        isEmpty.set(list.isEmpty())
    }
}