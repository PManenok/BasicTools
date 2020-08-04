package by.esas.tools.domain.mapper

open class ErrorModel<E : Enum<E>>(
    override val code: Int,
    override val statusEnum: E
) : IErrorModel<E> {
    override fun toString(): String {
        return "ErrorModel(code=$code, statusEnum=${statusEnum.name})"
    }
}
