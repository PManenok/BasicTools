package by.esas.tools.usecase

import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.domain.exception.BaseException
import by.esas.tools.domain.exception.BaseStatusEnum
import kotlin.coroutines.CoroutineContext

class GetDefaultCardUseCase constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext
) : BaseUseCase<String>(errorUtil, null) {
    override val TAG: String = GetDefaultCardUseCase::class.java.simpleName

    override suspend fun executeOnBackground(): String {
        throw BaseException(BaseStatusEnum.UNKNOWN_ERROR)
    }
}