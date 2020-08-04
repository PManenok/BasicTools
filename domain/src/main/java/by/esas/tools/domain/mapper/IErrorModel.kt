package by.esas.tools.domain.mapper

interface IErrorModel<E : Enum<E>> {
    val code: Int
    val statusEnum: E
}
