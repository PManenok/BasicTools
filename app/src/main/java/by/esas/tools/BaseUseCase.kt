package by.esas.tools

import by.esas.tools.domain.usecase.IRefresh
import by.esas.tools.domain.usecase.UseCase

abstract class BaseUseCase<T>(errorUtil: AppErrorMapper, refresher: IRefresh<AppErrorStatusEnum, ErrorModel>?) :
    UseCase<T, AppErrorStatusEnum, ErrorModel>(errorUtil, refresher)