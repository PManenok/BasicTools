/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.mvvm

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.BaseVMLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorData
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util.SwitchManager

abstract class BaseViewModel<E : Enum<E>, M : BaseErrorModel<E>> : ViewModel() {
    open val TAG: String = BaseViewModel::class.java.simpleName

    open var logger: ILogger<E, M> = BaseVMLogger(TAG, null)

    var switchableViewsList: () -> List<View?> = { emptyList() }
    protected open var switcher: SwitchManager = SwitchManager()

    val progressing = ObservableBoolean(false)
    val errorData: MutableLiveData<ErrorData<E, M>> = MutableLiveData<ErrorData<E, M>>()

    open fun hideProgress() {
        progressing.set(false)
    }

    open fun showProgress() {
        progressing.set(true)
    }

    override fun onCleared() {
        super.onCleared()
        logger.log("onCleared")
    }

    /**
     * Блокируем редактирование полей.
     */
    open fun disableControls(): Boolean {
        showProgress()
        var result: Boolean = true
        switchableViewsList().forEach { view ->
            view?.let { result = switcher.disableView(it) && result }
        }
        return result
    }

    /**
     * Возвращаем возможность редактировать поля.
     */
    open fun enableControls(): Boolean {
        hideProgress()
        var result: Boolean = true
        switchableViewsList().forEach { view ->
            view?.let { result = switcher.enableView(it) && result }
        }
        return result
    }

    open fun handleError(
        error: Throwable,
        showType: ShowErrorType = ShowErrorType.SHOW_ERROR_DIALOG,
        actionName: String? = null,
        doOnDialogOK: () -> Unit = {}
    ) {
        errorData.postValue(ErrorData(throwable = error, showType = showType, doOnDialogOK = doOnDialogOK, actionName = actionName))
    }

    open fun handleError(
        error: M,
        showType: ShowErrorType = ShowErrorType.SHOW_ERROR_DIALOG,
        actionName: String? = null,
        doOnDialogOK: () -> Unit = {}
    ) {
        errorData.postValue(ErrorData(model = error, showType = showType, doOnDialogOK = doOnDialogOK, actionName = actionName))
    }

    open fun handleAction(actionName: String?) {
        logger.log("handleAction $actionName")
    }
}