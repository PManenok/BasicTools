package by.esas.tools.screens.menu

import android.util.Log
import by.esas.tools.simple.AppVM
import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.screens.menu.recycler.CaseItemInfo
import javax.inject.Inject

class MenuVM @Inject constructor(val mapper: AppErrorMapper) : AppVM() {
    override fun provideMapper(): IErrorMapper<ErrorModel> {
        return mapper
    }

    val allCases = listOf(
        CaseItemInfo(0, "Case1", listOf("Cardline", "Dialog")),
        CaseItemInfo(0, "Case1", listOf("Cardline", "Dialog")),
        CaseItemInfo(0, "Case1", listOf("Cardline", "Dialog, ListHeader"))
    )

    val caseAdapter = CaseAdapter(
        onClick = {
            logger.logInfo("item click")
        }
    )

    fun updateAdapter(list: List<CaseItemInfo>){
        caseAdapter.cleanItems()
        caseAdapter.addItems(list)
    }
}