package by.esas.tools.app_domain.usecase

import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.domain.exception.BaseException
import by.esas.tools.domain.exception.BaseStatusEnum
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class PlayRandomUseCase @Inject constructor(
    errorUtil: AppErrorMapper,
    foregroundContext: CoroutineContext
): BaseUseCase<String>(errorUtil, foregroundContext){
    override val TAG = PlayRandomUseCase::class.java.simpleName

    var rangeNumbers = 0
    var number = 0

    override suspend fun executeOnBackground(): String {
        if (number == Random.nextInt(0, rangeNumbers+1)) {
            return App.appContext.getString(R.string.domain_case_congratulations)
        } else
            throw BaseException(BaseStatusEnum.UNKNOWN_ERROR)
    }
}