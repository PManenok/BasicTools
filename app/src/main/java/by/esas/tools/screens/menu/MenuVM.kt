package by.esas.tools.screens.menu

import androidx.databinding.ObservableBoolean
import androidx.navigation.NavDirections
import by.esas.tools.base.AppVM
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.entity.Modules
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
            listOf(Modules.PIN_VIEW, Modules.LISTHEADER),
            MenuFragmentDirections.actionMenuFragmentToPinViewFragment()
        )
        addCaseItem(
            "Check SavedState view model",
            listOf(Modules.BASE_DAGGER_UI, Modules.BASE_UI),
            MenuFragmentDirections.actionMenuFragmentToSavedStateFragment()
        )
        addCaseItem(
            "Check NumpadImageView functionality",
            listOf(Modules.NUMPAD),
            MenuFragmentDirections.actionMenuFragmentToNumpadImageFragment()
        )
        addCaseItem(
            "Check Keyboard functionality in Util module",
            listOf(Modules.UTIL),
            MenuFragmentDirections.actionMenuFragmentToUtilKeyboardFragment()
        )
        addCaseItem(
            "Check Utils functionality in Util module",
            listOf(Modules.UTIL),
            MenuFragmentDirections.actionMenuFragmentToUtilUtilsFragment()
        )
        addCaseItem(
            "Check SwitchManager functionality in Util module",
            listOf(Modules.UTIL, Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToUtilSwitchManagerFragment()
        )
        addCaseItem(
            "Check CustomSwitch functionality",
            listOf(Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToCustomSwitchFragment()
        )
        addCaseItem(
            "Create you CustomSwitch",
            listOf(Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToCustomSwitchProgramFragment()
        )
        addCaseItem(
            "Check Listheader functionality",
            listOf(Modules.LISTHEADER, Modules.CUSTOMSWITCH),
            MenuFragmentDirections.actionMenuFragmentToListheaderFragment()
        )
        addCaseItem(
            "Check Topbar view functionality",
            listOf(Modules.TOPBAR_VIEW),
            MenuFragmentDirections.actionMenuFragmentToTopbarFragment()
        )
        addCaseItem(
            "Check Cardline functionality",
            listOf(Modules.CARDLINE),
            MenuFragmentDirections.actionMenuFragmentToCardlineFragment()
        )
        addCaseItem(
            "Check PinView functionality",
            listOf(Modules.PIN_VIEW),
            MenuFragmentDirections.actionMenuFragmentToPinViewFragment()
        )
        updateAdapter(allCases)
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

    fun clearSearch(){
        updateAdapter(allCases)
        prevSearch = ""
    }

    private fun addCaseItem(
        name: String,
        modulesList: List<String>,
        direction: NavDirections? = null
    ) {
        allCases.add(CaseItemInfo(allCases.size, name, modulesList, direction))
    }

    private fun updateAdapter(list: List<CaseItemInfo>) {
        caseAdapter.addItems(list)
        isEmpty.set(list.isEmpty())
    }
}