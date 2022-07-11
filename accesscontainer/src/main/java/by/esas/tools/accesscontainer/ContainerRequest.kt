/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer

import by.esas.tools.logger.BaseErrorModel


class ContainerRequest<T, M : BaseErrorModel> {
    private var onComplete: ((T) -> Unit)? = null
    private var onError: ((M) -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var onCancellation: ((IllegalStateException) -> Unit)? = null

    fun onComplete(block: (T) -> Unit) {
        onComplete = block
    }

    fun onError(block: (M) -> Unit) {
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

    operator fun invoke(error: M) {
        onError?.invoke(error)
    }

    operator fun invoke() {
        onCancel?.invoke()
    }

    operator fun invoke(error: IllegalStateException) {
        onCancellation?.invoke(error)
    }
}