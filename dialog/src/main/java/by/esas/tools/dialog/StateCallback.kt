package by.esas.tools.dialog

interface StateCallback<T : Exception> {
    fun onError(e: T)
    fun onDismiss(afterOk: Boolean)
}