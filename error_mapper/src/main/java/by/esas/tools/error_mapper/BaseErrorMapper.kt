/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.error_mapper

import by.esas.tools.error_mapper.error.ApiErrorStatusEnum
import by.esas.tools.error_mapper.error.BaseStatusEnum
import by.esas.tools.error_mapper.error.ErrorMessageEnum
import by.esas.tools.error_mapper.error.HttpErrorStatusEnum
import by.esas.tools.error_mapper.error.IdentityErrorEnum
import by.esas.tools.error_mapper.response.ErrorCode
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.logger.ILogger
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

abstract class BaseErrorMapper<E : Enum<E>, Model : BaseErrorModel>(
    protected val moshi: Moshi,
    val logger: ILogger<Model>
) : IErrorMapper<Model> {

    open val TAG: String = BaseErrorMapper::class.java.simpleName
    protected val errorCodeAdapter: JsonAdapter<ErrorCode> =
        moshi.adapter<ErrorCode>(ErrorCode::class.java)

    override fun provideLogger(): ILogger<Model> {
        return logger
    }

    override fun mapErrorException(tag: String, throwable: Throwable?): Model {
        val errorModel: Model = when (throwable) {
            is HttpException -> {
                getHttpError(throwable)
            }

            is SocketTimeoutException -> {
                createModel(522, mapBaseException(BaseStatusEnum.NET_TIMEOUT.name))
            }

            is SSLException -> {
                createModel(523, mapBaseException(BaseStatusEnum.NET_NO_CONNECTION.name))
            }

            is UnknownHostException -> {
                createModel(523, mapBaseException(BaseStatusEnum.NET_UNKNOWN_HOST.name))
            }

            is IOException -> {
                createModel(523, mapBaseException(BaseStatusEnum.NET_NO_CONNECTION.name))
            }

            else -> {
                createModel(-1, mapBaseException(BaseStatusEnum.UNKNOWN_ERROR.name))
            }
        }
        if (throwable != null)
            provideLogger().logThrowable(tag, throwable)
        else
            provideLogger().logInfo("Throwable was null")
        return errorModel
    }

    //region Http error response mapping
    protected open fun getHttpError(throwable: HttpException): Model {
        return try {
            val response = throwable.response() as Response
            val responseBody = (response.body() ?: response.errorBody()) as ResponseBody?
            val body = responseBody?.string()

            logger.logDebug("getHttpError(): errorBody = [$body]")

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
            createModel(code = 0, status = BaseStatusEnum.NOT_SET.name)
        }
    }

    protected open fun parseResponse(throwable: HttpException?, body: String?): String {
        val errorCode: ErrorCode? = body?.let {
            try {
                errorCodeAdapter.fromJson(body)
            } catch (e: IOException) {
                logger.logThrowable(TAG, e)
                null
            }
        }
        val code: Long? = throwable?.code()?.toLong()
        return if (code != null && errorCode != null) {
            if (code == 401L) {
                HttpErrorStatusEnum.UNAUTHORIZED.name
            } else {
                extraHttpParse(code, errorCode)
                    ?: if (!errorCode.errorDescriptions.isNullOrBlank()) {
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

    protected open fun getStatusFromDescription(description: String): String {
        val errorStatusMessage = ErrorMessageEnum.entries.find {
            description.contains(it.message.toRegex())
        }
        return if (errorStatusMessage != null) mapErrorMessageToHttpStatus(errorStatusMessage).name
        else getStatusFromIdentityError(description)
    }

    protected open fun getStatusFromIdentityError(error: String): String {
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
    protected open fun getStatusFromMessage(message: String): String {
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

    protected open fun getHttpStatusFromMessage(message: String): String {
        val errorStatusMessage = ErrorMessageEnum.entries.find {
            message.contains(it.message.toRegex())
        }
        return mapErrorMessageToHttpStatus(errorStatusMessage).name
    }

    protected open fun getHttpStatusFromStatus(message: String): String {
        val status = try {
            ApiErrorStatusEnum.valueOf(message)
        } catch (e: IllegalStateException) {
            null
        }
        return mapApiErrorToHttpStatus(status).name
    }

    //endregion Http error response mapping

    abstract fun mapHttpStatus(httpEnumString: String): String
    abstract fun mapBaseException(errorText: String?): String
    open fun extraHttpParse(code: Long, errorCode: ErrorCode): String? {
        return null
    }
}

