package by.esas.tools.domain.mapper

import by.esas.tools.domain.exception.BaseException
import by.esas.tools.domain.exception.BaseStatusEnum
import by.esas.tools.domain.mapper.error.ApiErrorStatusEnum
import by.esas.tools.domain.mapper.error.ErrorMessageEnum
import by.esas.tools.domain.mapper.error.HttpErrorStatusEnum
import by.esas.tools.domain.mapper.error.IdentityErrorEnum
import by.esas.tools.domain.mapper.response.ErrorCode
import by.esas.tools.domain.util.ILogger
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

abstract class BaseErrorMapper<E : Enum<E>, Model : IErrorModel<E>>(protected val moshi: Moshi, val logger: ILogger<E>) {
    open val TAG: String = BaseErrorMapper::class.java.simpleName
    protected val errorCodeAdapter: JsonAdapter<ErrorCode> = moshi.adapter<ErrorCode>(ErrorCode::class.java)

    open fun mapErrorException(tag: String, throwable: Throwable?): Model {
        val errorModel: Model = when (throwable) {
            is BaseException -> {
                createModel(0, mapBaseException(throwable.message))
            }
            is HttpException -> {
                getHttpError(throwable)
            }
            is SocketTimeoutException -> {
                createModel(522, mapBaseStatus(BaseStatusEnum.NET_TIMEOUT))
            }
            is SSLException -> {
                createModel(523, mapBaseStatus(BaseStatusEnum.NET_NO_CONNECTION))
            }
            is IOException -> {
                createModel(523, mapBaseStatus(BaseStatusEnum.NET_NO_CONNECTION))
            }
            is UnknownHostException -> {
                createModel(523, mapBaseStatus(BaseStatusEnum.NET_UNKNOWN_HOST))
            }
            else -> {
                createModel(0, mapBaseStatus(BaseStatusEnum.UNKNOWN_ERROR))
            }
        }
        logThrowable(tag, throwable, errorModel.statusEnum)
        return errorModel
    }

    protected open fun getHttpError(throwable: HttpException): Model {
        return try {
            val response = throwable.response() as Response
            val responseBody = (response.body() ?: response.errorBody()) as ResponseBody?
            val body = responseBody?.string()

            logger.logLocally("getHttpError(): errorBody = [$body]")

            val error = parseResponse(throwable, body)

            when {
                (throwable.code() >= 400) and (throwable.code() < 500) ->
                    createModel(throwable.code(), mapHttpStatus(error))
                throwable.code() >= 500 ->
                    createModel(throwable.code(), mapHttpStatus(error))
                else -> createModel(throwable.code(), mapHttpStatus(error))
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            logger.logError(e)
            createModel(code = 0, status = mapBaseStatus(BaseStatusEnum.NOT_SET))
        }
    }

    protected open fun parseResponse(throwable: HttpException?, body: String?): String {
        val errorCode: ErrorCode? = body?.let {
            try {
                errorCodeAdapter.fromJson(body)
            } catch (e: IOException) {
                logger.logError(e)
                null
            }
        }
        val code: Long? = throwable?.code()?.toLong()
        return if (code != null && errorCode != null) {
            if (code == 401L) {
                HttpErrorStatusEnum.UNAUTHORIZED.name
            } else {
                extraHttpParse(code, errorCode) ?: if (!errorCode.errorDescriptions.isNullOrBlank()) {
                    getStatusFromDescription(errorCode.errorDescriptions)
                } else if (!errorCode.error.isNullOrBlank()) {
                    getStatusFromIdentityError(errorCode.error)
                } else if (!errorCode.message.isNullOrBlank()) {
                    getStatusFromMessage(errorCode.message)
                } else {
                    HttpErrorStatusEnum.UNKNOWN_ERROR.name
                }

            }
        } else HttpErrorStatusEnum.UNKNOWN_ERROR.name
    }

    private fun getStatusFromDescription(description: String): String {
        val errorStatusMessage = ErrorMessageEnum.values().find {
            description.contains(it.message.toRegex())
        }
        return if (errorStatusMessage != null) mapErrorMessageToHttpStatus(errorStatusMessage).name
        else getStatusFromIdentityError(description)
    }

    private fun getStatusFromIdentityError(error: String): String {
        val status = try {
            IdentityErrorEnum.valueOf(error)
        } catch (e: IllegalStateException) {
            null
        }
        return mapIdentityErrorToHttpStatus(status).name
    }

    /**
     * Get status from message. Message comes from StatusCode + can be combined with ErrorStatusMessage
     * */
    private fun getStatusFromMessage(message: String): String {
        return if (message.contains(":")) {
            val status = message.substringBefore(":").trim()
            val info = message.substringAfter(":").trim()

            if (info.isNotBlank()) {
                val messageStatus: String = getHttpStatusFromMessage(info)
                if (messageStatus == HttpErrorStatusEnum.CLIENT_ERROR.name || messageStatus == HttpErrorStatusEnum.SERVER_ERROR.name)
                    getHttpStatusFromStatus(status)
                else messageStatus
            } else getHttpStatusFromStatus(status)
        } else {
            getHttpStatusFromStatus(message)
        }
    }

    private fun getHttpStatusFromMessage(message: String): String {
        val errorStatusMessage = ErrorMessageEnum.values().find {
            message.contains(it.message.toRegex())
        }
        return mapErrorMessageToHttpStatus(errorStatusMessage).name
    }

    private fun getHttpStatusFromStatus(message: String): String {
        val status = try {
            ApiErrorStatusEnum.valueOf(message)
        } catch (e: IllegalStateException) {
            null
        }
        return mapApiErrorToHttpStatus(status).name
    }

    open fun logThrowable(tag: String, throwable: Throwable?, status: Enum<*>? = null) {
        logger.setTag(tag)
        throwable?.let { logger.logError(throwable) }
    }

    open fun logThrowable(throwable: Throwable?, status: Enum<*>? = null) {
        logThrowable(TAG, throwable, status)
    }

    abstract fun mapBaseException(errorText: String?): E
    abstract fun mapHttpStatus(httpEnumString: String): E
    abstract fun mapBaseStatus(enum: BaseStatusEnum): E
    abstract fun createModel(code: Int, status: E): Model
    open fun extraHttpParse(code: Long, errorCode: ErrorCode): String? {
        return null
    }
}

