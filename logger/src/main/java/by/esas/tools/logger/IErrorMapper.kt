package by.esas.tools.logger

interface IErrorMapper<M : BaseErrorModel> {

    fun mapErrorException(tag: String, throwable: Throwable?): M

    fun createModel(code: Int, status: String): M
}