package by.esas.tools.screens.menu

import by.esas.tools.base.AppVM
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.screens.menu.recycler.CaseItemInfo
import javax.inject.Inject

class MenuVM @Inject constructor() : AppVM() {

    val allCases = listOf(
        CaseItemInfo(
            0,
            "Check PinView functionality",
            listOf("pinview"),
            MenuFragmentDirections.actionMenuFragmentToPinViewFragment()
        ),
        CaseItemInfo(
            1,
            "Check SavedState view model",
            listOf("basedaggerui", "baseui"),
            MenuFragmentDirections.actionMenuFragmentToSavedStateFragment()
        ),
        CaseItemInfo(0, "Case1", listOf("Cardline", "Dialog")),
        CaseItemInfo(0, "Case1", listOf("Cardline", "Dialog")),
        CaseItemInfo(0, "Case1", listOf("Cardline", "Dialog, ListHeader"))
    )

    val caseAdapter = CaseAdapter(
        onClick = { item ->
            logger.logInfo("${item.name} clicked")
            item.direction?.let { navigate(it) }
        }
    )

    fun updateAdapter(list: List<CaseItemInfo>){
        caseAdapter.cleanItems()
        caseAdapter.addItems(list)
    }
}