/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.domain.usecase

import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.logger.ILogger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

//typealias CompletionBlock<T, E,Model:IErrorModel<E>> = Request<T, E,Model:IErrorModel<E>>.() -> Unit

abstract class UseCase<T, Model : BaseErrorModel>(
    protected val errorUtil: IErrorMapper<Model>,
    protected val refresher: IRefresh<Model>?
) {

    protected abstract val TAG: String
    protected open val logger: ILogger<Model> = ILogger<Model>().apply {
        setTag(TAG)
    }
    protected var parentJob: Job = Job()
    lateinit var foregroundContext: CoroutineContext
    var backgroundContext: CoroutineContext = Dispatchers.IO

    protected var _cancellationCallback: CancellationCallback? = null
    fun setCancellationCallback(callback: CancellationCallback?) {
        _cancellationCallback = callback
    }

    constructor(
        errorUtil: IErrorMapper<Model>,
        foregroundContext: CoroutineContext,
        backgroundContext: CoroutineContext = Dispatchers.IO
    ) : this(errorUtil, null) {
        this.foregroundContext = foregroundContext
        this.backgroundContext = backgroundContext
    }

    constructor(
        errorUtil: IErrorMapper<Model>,
        refresher: IRefresh<Model>,
        foregroundContext: CoroutineContext,
        backgroundContext: CoroutineContext = Dispatchers.IO
    ) : this(errorUtil, refresher) {
        this.foregroundContext = foregroundContext
        this.backgroundContext = backgroundContext
    }

    abstract suspend fun executeOnBackground(): T

    open fun execute(block: Request<T, Model>.() -> Unit) {
        val response = createRequest(block)
        unsubscribe()
        parentJob = Job()
        CoroutineScope(foregroundContext + parentJob).launch {
            try {
                val result = withContext(backgroundContext) {
                    executeOnBackground()
                }
                response(result)
            } catch (cancellationException: CancellationException) {
                response(cancellationException)
            } catch (e: Exception) {
                val error = errorUtil.mapErrorException(TAG, e)
                if (error.code == 401 && refresher != null) {
                    refresh(block) // added refresh after 401
                } else response(error)
            }
        }
    }

    open fun createRequest(block: Request<T, Model>.() -> Unit): Request<T, Model> {
        return Request<T, Model>().apply { block() }
    }

    protected open fun refresh(block: Request<T, Model>.() -> Unit) {
        val response = Request<T, Model>().apply { block() }
        refresher?.refresh(
            repeat = {
                execute(block)
            }, onError = {
                response(it)
            },
            onCancel = {
                _cancellationCallback?.onCancel() ?: refresher.onCancel()
            }
        )
    }

    open fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }
}