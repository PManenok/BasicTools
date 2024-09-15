package by.esas.tools.app_domain.usecase

import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.app_domain.error_mapper.BaseException
import by.esas.tools.error_mapper.error.BaseStatusEnum
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