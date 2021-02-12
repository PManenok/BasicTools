package by.esas.tools.logger.handler

import by.esas.tools.logger.BaseErrorModel

abstract class ErrorHandler<E : Enum<E>, M: BaseErrorModel<E>> {
    abstract fun getErrorMessage(error: M): String
    abstract fun getErrorMessage(e: Throwable): String
    abstract fun mapError(e: Throwable): M
}