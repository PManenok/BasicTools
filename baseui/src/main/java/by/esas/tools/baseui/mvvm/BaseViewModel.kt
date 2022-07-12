/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.mvvm

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.esas.tools.logger.*
import by.esas.tools.logger.handler.ErrorAction
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util.TAGk

abstract class BaseViewModel<M : BaseErrorModel> : ViewModel() {

    open val TAG: String = BaseViewModel::class.java.simpleName

    open var logger: ILogger<*> = BaseLoggerImpl(BaseViewModel::class.java.simpleName)
    abstract fun provideMapper(): IErrorMapper<M>

    val progressing = MutableLiveData<Boolean>(false)
    val action: MutableLiveData<Action?> = MutableLiveData<Action?>(null)
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
        logger.logOrder("handleError errorModel")
        action.postValue(ErrorAction.create(error, showType, actionName, parameters))
    }

    open fun requestAction(action: Action) {
        logger.logInfo("requestAction $action")
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
        logger.logOrder("handleAction $action")
        return false
    }

    //endregion handling methods

    //region helping methods

    open fun hideProgress() {
        logger.logOrder("hideProgress")
        progressing.postValue(false)
    }

    open fun showProgress() {
        logger.logOrder("showProgress")
        progressing.postValue(true)
    }

    /**
     * Block possibility to interact with UI (buttons, fields end etc. if other is not set)
     * Also shows progress and requests disable action
     */
    open fun disableControls(sendAction: Boolean = true) {
        logger.logOrder("disableControls")
        showProgress()
        controlsEnabled.postValue(false)
        if (sendAction)
            action.postValue(Action(Action.ACTION_DISABLE_CONTROLS))
    }

    /**
     * Return possibility to interact with UI (buttons, fields end etc. if other is not set)
     * Also hides progress and requests enable action
     */
    open fun enableControls(sendAction: Boolean = true) {
        logger.logOrder("enableControls")
        if (sendAction)
            action.postValue(Action(Action.ACTION_ENABLE_CONTROLS))
        controlsEnabled.postValue(true)
        hideProgress()
    }

    //endregion helping methods
}