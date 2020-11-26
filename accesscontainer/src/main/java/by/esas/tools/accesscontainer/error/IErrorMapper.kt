package by.esas.tools.accesscontainer.error

import by.esas.tools.logger.BaseErrorModel

interface IErrorMapper<E : Enum<E>, M : BaseErrorModel<E>> {
    fun mapError(e: Exception): M

    operator fun invoke(e: Exception): M {
        return mapError(e)
    }
}