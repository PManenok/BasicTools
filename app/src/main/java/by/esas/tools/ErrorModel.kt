package by.esas.tools

import by.esas.tools.domain.mapper.IErrorModel

class ErrorModel(override val code: Int, override val statusEnum: AppErrorStatusEnum) : IErrorModel<AppErrorStatusEnum>