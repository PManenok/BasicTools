package by.esas.tools.app_domain.usecase

import by.esas.tools.app_data.db.dao.CaseStatusDao
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.entity.CaseStatus
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetSavedCaseStatusUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext,
    private val dao: CaseStatusDao
) : BaseUseCase<CaseStatus>(errorUtil, foregroundContext) {

    override val TAG: String = GetCaseItemsUseCase::class.java.simpleName

    var caseId = -1

    override suspend fun executeOnBackground(): CaseStatus {

        return dao.getById(caseId)
    }
}