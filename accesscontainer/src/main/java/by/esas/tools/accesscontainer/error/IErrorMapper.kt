package by.esas.tools.accesscontainer.error

interface IErrorMapper<Model> {
    fun mapError(e: Exception): Model

    operator fun invoke(e: Exception): Model {
        return mapError(e)
    }
}