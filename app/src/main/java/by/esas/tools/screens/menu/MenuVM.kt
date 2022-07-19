package by.esas.tools.screens.menu

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
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

    var prevSearch = ""

    val allCases = listOf(
        CaseItemInfo(0, "case", listOf("case", "hanna")),
        CaseItemInfo(0, "lase", listOf("Cardline", "Dialog")),
        CaseItemInfo(0, "hanna", listOf("Cardline", "Dialog, ListHeader")),
        CaseItemInfo(0, "calee", listOf("Cardline", "case")),
        CaseItemInfo(0, "honn", listOf("Dialog", "case")),
        CaseItemInfo(0, "ccase", listOf("Dialog", "Dialog")),
        CaseItemInfo(0, "lasee", listOf("Cardline", "hanna"))
    )

    val caseAdapter = CaseAdapter(
        onClick = {
            logger.logInfo("item click")
        }
    )

    fun updateAdapter(list: List<CaseItemInfo>) {
        caseAdapter.cleanItems()
        caseAdapter.addItems(list)
    }

    fun onSearchChanged(value: String) {
        if (prevSearch != value) {
            disableControls()
            prevSearch = value
            val searchList = splitSearch(value)
            val list = doSearch(allCases, searchList)
            if (list.size != caseAdapter.itemCount) {
                caseAdapter.addItems(list)
            }
            enableControls()
        }
    }

    private fun doSearch(list: List<CaseItemInfo>, searchList: List<String>): List<CaseItemInfo> {
        if (prevSearch.isNotEmpty()) {
            var filteredList = list
            for (i in searchList) {
                filteredList = filteredList.filter { item ->
                    item.name.contains(i, true) || doSearchByModules(item, i)
                }
            }
            return filteredList
        }
        return list
    }

    private fun doSearchByModules(case: CaseItemInfo, search: String): Boolean {
        val list = case.modules.filter { module ->
            module.contains(search, true)
        }
        return list.isNotEmpty()
    }

    private fun splitSearch(search: String): List<String> {
        return search.split(" ", ",", ";").filter { it != "" }
    }
}