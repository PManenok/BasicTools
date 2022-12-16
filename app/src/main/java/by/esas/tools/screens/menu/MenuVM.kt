package by.esas.tools.screens.menu

import androidx.databinding.ObservableBoolean
import by.esas.tools.base.AppVM
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.usecase.SearchCaseUseCase
import javax.inject.Inject

class MenuVM @Inject constructor(
    val searchCase: SearchCaseUseCase
) : AppVM() {

    var prevSearch = ""
    var search = ""
    var isEmpty = ObservableBoolean(false)

    val allCases: MutableList<CaseItemInfo> = mutableListOf()

    val caseAdapter = CaseAdapter(
        onClick = { item ->
            logger.logInfo("${item.name} clicked")
            item.direction?.let { navigate(it) }
        }
    )

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
        caseAdapter.setItems(list)
        isEmpty.set(list.isEmpty())
    }
}