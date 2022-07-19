package by.esas.tools.app_domain.usecase

import by.esas.tools.domain.exception.BaseException
import by.esas.tools.domain.exception.BaseStatusEnum
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GetDefaultCardUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext
) : BaseUseCase<String>(errorUtil, foregroundContext) {
    override val TAG: String = GetDefaultCardUseCase::class.java.simpleName

    override suspend fun executeOnBackground(): String {
        delay(1000L)
        throw BaseException(BaseStatusEnum.UNKNOWN_ERROR)
    }
}