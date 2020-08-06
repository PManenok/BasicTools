package by.esas.tools

import by.esas.tools.logger.IErrorModel

class ErrorModel(override val code: Int, override val statusEnum: AppErrorStatusEnum) : IErrorModel<AppErrorStatusEnum>