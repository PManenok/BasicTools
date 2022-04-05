/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.mvvm

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.BaseLoggerImpl
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorAction
import by.esas.tools.logger.handler.ShowErrorType

abstract class BaseViewModel<E : Enum<E>, M : BaseErrorModel<E>> : ViewModel() {
    open val TAG: String = BaseViewModel::class.java.simpleName
    open var logger: ILogger<E, M> = BaseLoggerImpl(BaseViewModel::class.java.simpleName)

    val progressing = ObservableBoolean(false)
    val errorAction: MutableLiveData<ErrorAction<E, M>?> = MutableLiveData<ErrorAction<E, M>?>(null)
    val requestAction: MutableLiveData<Action?> = MutableLiveData<Action?>(null)
    val controlsEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    override fun onCleared() {
        super.onCleared()
        logger.logOrder("onCleared")
    }

    //region handling methods

    open fun handleError(
        error: Throwable,
        showType: String = ShowErrorType.SHOW_ERROR_DIALOG.name,
        actionName: String? = null,
        parameters: Bundle? = null
    ) {
        logger.logOrder("handleError throwable")
        errorAction.postValue(ErrorAction.create(error, null, showType, actionName, parameters))
    }

    open fun handleError(
        error: M,
        showType: String = ShowErrorType.SHOW_ERROR_DIALOG.name,
        actionName: String? = null,
        parameters: Bundle = Bundle()
    ) {
        logger.logOrder("handleError errorModel")
        errorAction.postValue(ErrorAction.create(null, error, showType, actionName, parameters))
    }

    open fun requestAction(action: Action) {
        requestAction.postValue(action)
    }

    open fun handleAction(action: Action?): Boolean {
        logger.logOrder("handleAction $action")
        return false
    }

    //endregion handling methods

    //region helping methods

    open fun hideProgress() {
        logger.logOrder("hideProgress")
        progressing.set(false)
    }

    open fun showProgress() {
        logger.logOrder("showProgress")
        progressing.set(true)
    }

    /**
     * Block possibility to interact with UI (buttons, fields end etc. if other is not set)
     * Also shows progress and requests disable action
     */
    open fun disableControls() {
        logger.logOrder("disableControls")
        showProgress()
        controlsEnabled.postValue(false)
        requestAction.postValue(Action(Action.ACTION_DISABLE_CONTROLS))
    }

    /**
     * Return possibility to interact with UI (buttons, fields end etc. if other is not set)
     * Also hides progress and requests enable action
     */
    open fun enableControls() {
        logger.logOrder("enableControls")
        hideProgress()
        controlsEnabled.postValue(true)
        requestAction.postValue(Action(Action.ACTION_ENABLE_CONTROLS))
    }

    //endregion helping methods
}