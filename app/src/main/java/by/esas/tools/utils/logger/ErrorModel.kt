package by.esas.tools.utils.logger

import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import by.esas.tools.logger.BaseErrorModel

class ErrorModel(code: Int, statusEnum: AppErrorStatusEnum) : BaseErrorModel(code, statusEnum.name) {

    fun getStatusAsEnum(): AppErrorStatusEnum {
        return AppErrorStatusEnum.getAppErrorStatusEnum(status)
    }
}