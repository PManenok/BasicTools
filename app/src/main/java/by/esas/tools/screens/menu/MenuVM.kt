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
        )
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