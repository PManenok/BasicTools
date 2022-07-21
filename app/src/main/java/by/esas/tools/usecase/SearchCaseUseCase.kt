package by.esas.tools.usecase

import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.error_mapper.AppErrorMapper
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SearchCaseUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext
): BaseUseCase<List<CaseItemInfo>>(errorUtil, foregroundContext) {
    override val TAG: String = SearchCaseUseCase::class.java.simpleName

    var caseItems = listOf<CaseItemInfo>()
    var search = ""

    override suspend fun executeOnBackground(): List<CaseItemInfo> {
        if (search != "") {
            var filteredList = caseItems
            val searchList = splitSearch(search)
            for (i in searchList) {
                filteredList = filteredList.filter { item ->
                    item.name.contains(i, true) || doSearchByModules(item, i)
                }
            }
            return filteredList
        } else {
            return caseItems
        }
    }

    private fun splitSearch(search: String): List<String> {
        val splitList = search.split(";")
        val searchItemsList = arrayListOf<String>()
        splitList.forEach { item ->
            searchItemsList.add(item.trim())
        }
        return searchItemsList.filter { it != "" }
    }

    private fun doSearchByModules(case: CaseItemInfo, search: String): Boolean {
        val list = case.modules.filter { module ->
            module.name.lowercase() == search.lowercase()
        }
        return list.isNotEmpty()
    }
}