package by.esas.tools.logger

interface IErrorModel<E : Enum<E>> {
    val code: Int
    val statusEnum: E
}
