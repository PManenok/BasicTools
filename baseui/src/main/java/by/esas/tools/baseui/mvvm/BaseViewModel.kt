/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.mvvm

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorAction
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util.TAGk

abstract class BaseViewModel<M : BaseErrorModel> : ViewModel() {

    open val TAG: String = TAGk

    open var logger: ILogger<*> = ILogger<BaseErrorModel>().apply {
        setTag(TAG)
    }
    abstract fun provideMapper(): IErrorMapper<M>

    val action: MutableLiveData<Action?> = MutableLiveData<Action?>(null)
    open val progressing = MutableLiveData<Boolean>(false)
    open val controlsEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    override fun onCleared() {
        super.onCleared()
        logger.order(TAG, "onCleared")
    }

    //region handling methods

    open fun handleError(
        error: Throwable,
        showType: String = ShowErrorType.SHOW_ERROR_DIALOG.name,
        actionName: String? = null,
        parameters: Bundle? = null
    ) {
        logger.order(TAG, "handleError throwable")
        action.postValue(
            ErrorAction.create(
                provideMapper().mapErrorException(TAGk, error),
                showType,
                actionName,
                parameters
            )
        )
    }

    open fun handleError(
        error: M,
        showType: String = ShowErrorType.SHOW_ERROR_DIALOG.name,
        actionName: String? = null,
        parameters: Bundle = Bundle()
    ) {
        logger.order(TAG, "handleError errorModel")
        action.postValue(ErrorAction.create(error, showType, actionName, parameters))
    }

    open fun requestAction(action: Action) {
        logger.i(TAG, "requestAction $action")
        this.action.postValue(action)
    }

    /**
     * Method handles action. This method do not send action elsewhere
     * and if it could not handle action method returns false
     *
     * @return Boolean
     *   true if action was handled
     *   false if action was not handled
     *   By default returns false
     */
    open fun handleAction(action: Action?): Boolean {
        logger.order(TAG, "handleAction $action")
        return false
    }

    //endregion handling methods

    //region helping methods

    open fun hideProgress() {
        logger.order(TAG, "hideProgress")
        progressing.postValue(false)
    }

    open fun showProgress() {
        logger.order(TAG, "showProgress")
        progressing.postValue(true)
    }

    /**
     * Block possibility to interact with UI (buttons, fields end etc. if other is not set)
     * Also shows progress
     */
    open fun disableControls() {
        logger.order(TAG, "disableControls")
        showProgress()
        controlsEnabled.postValue(false)
    }

    /**
     * Return possibility to interact with UI (buttons, fields end etc. if other is not set)
     * Also hides progress
     */
    open fun enableControls() {
        logger.order(TAG, "enableControls")
        controlsEnabled.postValue(true)
        hideProgress()
    }

    //endregion helping methods
}