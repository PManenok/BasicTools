package by.esas.tools.app_domain.usecase

import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.entity.TestStatusEnum
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FilterCaseUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext
) : BaseUseCase<List<CaseItemInfo>>(errorUtil, foregroundContext) {

    override val TAG = FilterCaseUseCase::class.java.simpleName

    var modules = listOf<String>()
    var statuses = listOf<TestStatusEnum>()
    var caseItems = listOf<CaseItemInfo>()

    override suspend fun executeOnBackground(): List<CaseItemInfo> {
        var filteredList = caseItems
        if (statuses.isNotEmpty())
            filteredList = filteredList.filter { it.status in statuses }
        if (modules.isNotEmpty())
            filteredList = filteredList.filter { it.modules.intersect(modules.toSet()).isNotEmpty() }

        return filteredList
    }
}