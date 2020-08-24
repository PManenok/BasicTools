package by.esas.tools.error_mapper

import by.esas.tools.domain.exception.BaseStatusEnum
import by.esas.tools.domain.mapper.BaseErrorMapper
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.ILogger
import com.squareup.moshi.Moshi
import retrofit2.HttpException

class AppErrorMapper(moshi: Moshi, logger: ILogger<AppErrorStatusEnum>) : BaseErrorMapper<AppErrorStatusEnum, ErrorModel>(moshi, logger) {
    override fun mapBaseException(errorText: String?): AppErrorStatusEnum {
        return AppErrorStatusEnum.APP_UNPREDICTED_ERROR
    }

    override fun mapBaseStatus(enum: BaseStatusEnum): AppErrorStatusEnum {
        return AppErrorStatusEnum.APP_UNPREDICTED_ERROR
    }

    override fun mapErrorException(tag: String, throwable: Throwable?): ErrorModel {
        val model = super.mapErrorException(tag, throwable)
        return ErrorModel(model.code, model.statusEnum)
    }

    override fun getHttpError(throwable: HttpException): ErrorModel {
        val model = super.getHttpError(throwable)
        return ErrorModel(model.code, model.statusEnum)
    }

    override fun createModel(code: Int, status: AppErrorStatusEnum): ErrorModel {
        return ErrorModel(code, status)
    }

    override fun mapHttpStatus(httpEnumString: String): AppErrorStatusEnum {
        return AppErrorStatusEnum.APP_UNPREDICTED_ERROR
    }
}