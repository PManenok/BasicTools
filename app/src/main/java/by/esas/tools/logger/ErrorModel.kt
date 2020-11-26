package by.esas.tools.logger

import by.esas.tools.error_mapper.AppErrorStatusEnum

class ErrorModel(code: Int, statusEnum: AppErrorStatusEnum) : BaseErrorModel<AppErrorStatusEnum>(code, statusEnum)