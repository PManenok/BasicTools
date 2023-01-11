package by.esas.tools.app_domain.usecase

import by.esas.tools.app_data.db.dao.CaseStatusDao
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.entity.CaseStatus
import by.esas.tools.entity.TestStatusEnum
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AddCaseStatusUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext,
    private val dao: CaseStatusDao
) : BaseUseCase<Unit>(errorUtil, foregroundContext) {
    override val TAG: String = GetCaseItemsUseCase::class.java.simpleName

    var caseId = -1
    var caseStatus = TestStatusEnum.UNCHECKED

    override suspend fun executeOnBackground() {
        dao.insert(CaseStatus(caseId, caseStatus))
    }
}