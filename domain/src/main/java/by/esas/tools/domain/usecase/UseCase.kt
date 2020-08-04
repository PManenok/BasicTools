package by.esas.tools.domain.usecase

import by.esas.tools.domain.mapper.BaseErrorMapper
import by.esas.tools.domain.mapper.IErrorModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

//typealias CompletionBlock<T, E,Model:IErrorModel<E>> = Request<T, E,Model:IErrorModel<E>>.() -> Unit

abstract class UseCase<T, E : Enum<E>, Model : IErrorModel<E>>(
    protected val errorUtil: BaseErrorMapper<E, Model>,
    protected val refresher: IRefresh<E, Model>?
) {
    protected abstract val TAG: String
    private var parentJob: Job = Job()
    lateinit var foregroundContext: CoroutineContext
    var backgroundContext: CoroutineContext = Dispatchers.IO

    private var cancellationCallback: CancellationCallback? = null
    fun setCancellationCallback(callback: CancellationCallback?) {
        this.cancellationCallback = callback
    }

    constructor(
        errorUtil: BaseErrorMapper<E, Model>,
        foregroundContext: CoroutineContext,
        backgroundContext: CoroutineContext = Dispatchers.IO
    ) : this(errorUtil, null) {
        this.foregroundContext = foregroundContext
        this.backgroundContext = backgroundContext
    }

    constructor(
        errorUtil: BaseErrorMapper<E, Model>,
        refresher: IRefresh<E, Model>,
        foregroundContext: CoroutineContext,
        backgroundContext: CoroutineContext = Dispatchers.IO
    ) : this(errorUtil, refresher) {
        this.foregroundContext = foregroundContext
        this.backgroundContext = backgroundContext
    }

    abstract suspend fun executeOnBackground(): T

    open fun execute(block: Request<T, E, Model>.() -> Unit) {
        errorUtil.logger.setTag(TAG)
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

    open fun createRequest(block: Request<T, E, Model>.() -> Unit): Request<T, E, Model> {
        return Request<T, E, Model>().apply { block() }
    }

    private fun refresh(block: Request<T, E, Model>.() -> Unit) {
        val response = Request<T, E, Model>().apply { block() }
        refresher?.refresh(
            repeat = {
                execute(block)
            }, onError = {
                response(it)
            },
            onCancel = {
                cancellationCallback?.onCancel() ?: refresher.onCancel()
            }
        )
    }

    fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }
}