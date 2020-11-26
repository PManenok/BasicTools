package by.esas.tools.accesscontainer

import by.esas.tools.logger.BaseErrorModel


class ContainerRequest<T, E:Enum<E>> {
    private var onComplete: ((T) -> Unit)? = null
    private var onError: ((BaseErrorModel<E>) -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var onCancellation: ((IllegalStateException) -> Unit)? = null

    fun onComplete(block: (T) -> Unit) {
        onComplete = block
    }

    fun onError(block: (BaseErrorModel<E>) -> Unit) {
        onError = block
    }

    fun onCancel(block: () -> Unit) {
        onCancel = block
    }

    fun onCancellation(block: (IllegalStateException) -> Unit) {
        onCancellation = block
    }

    operator fun invoke(result: T) {
        onComplete?.invoke(result)
    }

    operator fun invoke(error: BaseErrorModel<E>) {
        onError?.invoke(error)
    }

    operator fun invoke() {
        onCancel?.invoke()
    }

    operator fun invoke(error: IllegalStateException) {
        onCancellation?.invoke(error)
    }
}