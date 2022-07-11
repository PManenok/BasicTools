package by.esas.tools.logger

import by.esas.tools.error_mapper.AppErrorStatusEnum

class ErrorModel(code: Int, statusEnum: AppErrorStatusEnum) : BaseErrorModel(code, statusEnum.name) {

    fun getStatusAsEnum(): AppErrorStatusEnum {
        return try {
            AppErrorStatusEnum.valueOf(statusEnum)
        } catch (e: IllegalStateException) {
            AppErrorStatusEnum.NOT_SET
        }
    }
}