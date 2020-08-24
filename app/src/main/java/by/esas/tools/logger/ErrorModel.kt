package by.esas.tools.logger

import by.esas.tools.error_mapper.AppErrorStatusEnum

class ErrorModel(override val code: Int, override val statusEnum: AppErrorStatusEnum) : IErrorModel<AppErrorStatusEnum>