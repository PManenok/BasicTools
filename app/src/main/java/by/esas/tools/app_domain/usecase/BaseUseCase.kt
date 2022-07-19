package by.esas.tools.app_domain.usecase

import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.domain.usecase.IRefresh
import by.esas.tools.domain.usecase.UseCase
import by.esas.tools.utils.logger.ErrorModel
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseUseCase<T>(errorUtil: AppErrorMapper, refresher: IRefresh<ErrorModel>?) :
    UseCase<T, ErrorModel>(errorUtil, refresher) {

    constructor(
        errorUtil: AppErrorMapper,
        foregroundContext: CoroutineContext,
        backgroundContext: CoroutineContext = Dispatchers.IO
    ) : this(errorUtil, null) {
        this.foregroundContext = foregroundContext
        this.backgroundContext = backgroundContext
    }

    constructor(
        errorUtil: AppErrorMapper,
        refresher: IRefresh<ErrorModel>,
        foregroundContext: CoroutineContext,
        backgroundContext: CoroutineContext = Dispatchers.IO
    ) : this(errorUtil, refresher) {
        this.foregroundContext = foregroundContext
        this.backgroundContext = backgroundContext
    }
}