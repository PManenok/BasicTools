package by.esas.tools.accesscontainer.error

import by.esas.tools.logger.BaseErrorModel

interface IErrorMapper<E : Enum<E>> {
    fun mapError(e: Exception): BaseErrorModel<E>

    operator fun invoke(e: Exception): BaseErrorModel<E> {
        return mapError(e)
    }
}