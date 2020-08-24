package by.esas.tools.usecase

import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.domain.usecase.IRefresh
import by.esas.tools.domain.usecase.UseCase
import by.esas.tools.logger.ErrorModel

abstract class BaseUseCase<T>(errorUtil: AppErrorMapper, refresher: IRefresh<AppErrorStatusEnum, ErrorModel>?) :
    UseCase<T, AppErrorStatusEnum, ErrorModel>(errorUtil, refresher)