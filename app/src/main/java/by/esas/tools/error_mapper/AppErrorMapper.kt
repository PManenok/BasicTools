package by.esas.tools.error_mapper

import by.esas.tools.domain.mapper.BaseErrorMapper
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.ILogger
import com.squareup.moshi.Moshi

class AppErrorMapper(moshi: Moshi, logger: ILogger<ErrorModel>) :
    BaseErrorMapper<AppErrorStatusEnum, ErrorModel>(moshi, logger) {
    override fun mapBaseException(errorText: String?): String {
        return AppErrorStatusEnum.APP_UNPREDICTED_ERROR.name
    }

    /*override fun mapErrorException(tag: String, throwable: Throwable?): ErrorModel {
        val model = super.mapErrorException(tag, throwable)
        return ErrorModel(model.code, model.getStatusAsEnum())
    }*/

    /*override fun getHttpError(throwable: HttpException): ErrorModel {
        val model = super.getHttpError(throwable)
        return ErrorModel(model.code, model.statusEnum)
    }*/

    override fun createModel(code: Int, status: String): ErrorModel {
        return ErrorModel(code, status.getAppErrorStatusEnum())
    }

    override fun mapHttpStatus(httpEnumString: String): String {
        return AppErrorStatusEnum.APP_UNPREDICTED_ERROR.name
    }
}