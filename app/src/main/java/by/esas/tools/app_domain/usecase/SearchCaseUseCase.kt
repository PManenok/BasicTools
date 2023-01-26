package by.esas.tools.app_domain.usecase

import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.entity.CaseItemInfo
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SearchCaseUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext
) : BaseUseCase<List<CaseItemInfo>>(errorUtil, foregroundContext) {
    override val TAG: String = SearchCaseUseCase::class.java.simpleName

    var caseItems = listOf<CaseItemInfo>()
    var search = ""

    override suspend fun executeOnBackground(): List<CaseItemInfo> {
        return if (search != "") {
            var filteredList = caseItems
            val searchList = splitSearch(search)
            for (i in searchList) {
                filteredList = filteredList.filter { item ->
                    item.name.contains(i, true) || doSearchByModules(item, i)
                }
            }
            filteredList
        } else {
            caseItems
        }
    }

    private fun splitSearch(search: String): List<String> {
        return search.split(";").mapNotNull { result -> result.trim().takeIf { it.isNotBlank() } }
    }

    private fun doSearchByModules(case: CaseItemInfo, search: String): Boolean {
        val module = case.modules.find { module ->
            module == search
        }
        return module != null
    }
}