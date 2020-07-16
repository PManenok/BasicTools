package by.esas.tools.accesscontainer

import by.esas.tools.accesscontainer.error.IModel


class ContainerRequest<T, E> {
    private var onComplete: ((T) -> Unit)? = null
    private var onError: ((IModel<E>) -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var onCancellation: ((IllegalStateException) -> Unit)? = null

    fun onComplete(block: (T) -> Unit) {
        onComplete = block
    }

    fun onError(block: (IModel<E>) -> Unit) {
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

    operator fun invoke(error: IModel<E>) {
        onError?.invoke(error)
    }

    operator fun invoke() {
        onCancel?.invoke()
    }

    operator fun invoke(error: IllegalStateException) {
        onCancellation?.invoke(error)
    }
}