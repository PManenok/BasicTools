package by.esas.tools.accesscontainer.error

import by.esas.tools.logger.IErrorModel

interface IErrorMapper<E : Enum<E>> {
    fun mapError(e: Exception): IErrorModel<E>

    operator fun invoke(e: Exception): IErrorModel<E> {
        return mapError(e)
    }
}