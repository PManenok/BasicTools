package by.esas.tools.app_domain.usecase

import by.esas.tools.app_data.db.dao.CaseStatusDao
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ClearAllSavedTestStatusesUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext,
    private val dao: CaseStatusDao
) : BaseUseCase<Unit>(errorUtil, foregroundContext) {

    override val TAG: String = GetCaseItemsUseCase::class.java.simpleName

    override suspend fun executeOnBackground() {
        dao.deleteAll()
    }
}