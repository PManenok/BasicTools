package by.esas.tools.domain.exception

class BaseException(message: String) : Exception(message) {
    override fun toString(): String {
        return "BaseException(messageEnum=$message)"
    }

    constructor(enum: Enum<*>) : this(enum.name)
}