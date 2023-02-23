/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.domain.usecase

import by.esas.tools.logger.BaseErrorModel
import java.util.concurrent.CancellationException

open class Request<T, Model : BaseErrorModel> {

    protected var onComplete: ((T) -> Unit)? = null
    protected open var onError: ((Model) -> Unit)? = null
    protected var onCancel: ((CancellationException) -> Unit)? = null

    fun onComplete(block: (T) -> Unit) {
        onComplete = block
    }

    fun onError(block: (Model) -> Unit) {
        onError = block
    }

    fun onCancel(block: (CancellationException) -> Unit) {
        onCancel = block
    }

    operator fun invoke(result: T) {
        onComplete?.invoke(result)
    }

    open operator fun invoke(error: Model) {
        onError?.invoke(error)
    }

    operator fun invoke(error: CancellationException) {
        onCancel?.invoke(error)
    }
}