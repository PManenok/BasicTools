package by.esas.tools.logger

interface IErrorMapper<M : BaseErrorModel> {
    fun mapErrorException(tag: String, throwable: Throwable?): M

    fun createModel(code: Int, status: String): M

    fun logThrowable(tag: String, throwable: Throwable?, status: String? = null)
    fun logThrowable(throwable: Throwable?, status: String? = null)

    fun setTagToLogger(tag: String)
}