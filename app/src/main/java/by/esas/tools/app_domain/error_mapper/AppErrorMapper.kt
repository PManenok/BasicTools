package by.esas.tools.app_domain.error_mapper

import by.esas.tools.domain.mapper.BaseErrorMapper
import by.esas.tools.logger.ILogger
import by.esas.tools.utils.logger.ErrorModel
import com.squareup.moshi.Moshi

class AppErrorMapper(moshi: Moshi, logger: ILogger<ErrorModel>) :
    BaseErrorMapper<AppErrorStatusEnum, ErrorModel>(moshi, logger) {

    override fun mapBaseException(errorText: String?): String {
        return AppErrorStatusEnum.APP_UNPREDICTED_ERROR.name
    }

    override fun createModel(code: Int, status: String): ErrorModel {
        return ErrorModel(code, AppErrorStatusEnum.getAppErrorStatusEnum(status))
    }

    override fun mapHttpStatus(httpEnumString: String): String {
        return AppErrorStatusEnum.APP_UNPREDICTED_ERROR.name
    }
}