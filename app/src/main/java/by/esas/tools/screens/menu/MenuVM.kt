package by.esas.tools.screens.menu

import androidx.databinding.ObservableBoolean
import androidx.navigation.NavDirections
import by.esas.tools.base.AppVM
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.entity.ModuleEnum
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

    init {
        addCaseItem(
            "Check PinView functionality",
            listOf(ModuleEnum.PIN_VIEW, ModuleEnum.LISTHEADER),
            MenuFragmentDirections.actionMenuFragmentToPinViewFragment()
        )
        addCaseItem(
            "Check SavedState view model",
            listOf(ModuleEnum.BASE_DAGGER_UI, ModuleEnum.BASE_UI),
            MenuFragmentDirections.actionMenuFragmentToSavedStateFragment()
        )
        addCaseItem(
            "Check NumpadImageView functionality",
            listOf(ModuleEnum.NUMPAD),
            MenuFragmentDirections.actionMenuFragmentToNumpadImageFragment()
        )
        addCaseItem(
            "Check NumpadTextView functionality",
            listOf(ModuleEnum.NUMPAD),
            MenuFragmentDirections.actionMenuFragmentToNumpadTextFragment()
        )
    }

    fun updateAdapter(list: List<CaseItemInfo>) {
        caseAdapter.addItems(list)
        isEmpty.set(list.isEmpty())
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

    private fun addCaseItem(
        name: String,
        modulesList: List<ModuleEnum>,
        direction: NavDirections? = null
    ) {
        allCases.add(CaseItemInfo(allCases.size, name, modulesList, direction))
    }
}