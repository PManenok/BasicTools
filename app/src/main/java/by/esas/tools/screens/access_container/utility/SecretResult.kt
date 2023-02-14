package by.esas.tools.screens.access_container.utility

class SecretResult<T>(
    val type: Result,
    val result: T? = null
)

enum class Result {
    SUCCESS_RESULT,
    ERROR_RESULT
}