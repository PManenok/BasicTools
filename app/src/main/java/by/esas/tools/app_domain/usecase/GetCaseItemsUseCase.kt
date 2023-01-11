package by.esas.tools.app_domain.usecase

import by.esas.tools.app_data.Cases
import by.esas.tools.app_data.db.dao.CaseStatusDao
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.entity.TestStatusEnum
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetCaseItemsUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext,
    private val dao: CaseStatusDao
) : BaseUseCase<List<CaseItemInfo>>(errorUtil, foregroundContext) {
    override val TAG: String = GetCaseItemsUseCase::class.java.simpleName

    override suspend fun executeOnBackground(): List<CaseItemInfo> {
        val cases = Cases.getAll()
        val savedCaseStatuses = dao.getAll()
        val caseItemsList = cases.map { caseInfo ->
            CaseItemInfo(
                caseInfo.id,
                caseInfo.name,
                caseInfo.modules,
                savedCaseStatuses.find { it.caseId == caseInfo.id }?.status ?: TestStatusEnum.UNCHECKED
            )
        }

        return caseItemsList
    }
}
