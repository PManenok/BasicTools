package by.esas.tools

import by.esas.tools.domain.exception.BaseException
import by.esas.tools.domain.exception.BaseStatusEnum
import by.esas.tools.domain.usecase.UseCase
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