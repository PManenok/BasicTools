/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.basic

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import by.esas.tools.basedaggerui.R
import by.esas.tools.basedaggerui.inject.factory.InjectingViewModelFactory
import by.esas.tools.basedaggerui.interfaces.IActionHandler
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.BaseLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorData
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util.SwitchManager
import by.esas.tools.util.defocusAndHideKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<E : Enum<E>, M : BaseErrorModel<E>> : DaggerFragment(), IActionHandler {
    companion object {
        val TAG: String = BaseFragment::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory

    protected open var logger: ILogger<E, *> = BaseLogger(TAG, null)
    protected open var hideKeyboardOnStop: Boolean = true
    protected open var switcher: SwitchManager = SwitchManager()
    protected var showError: Boolean = false
    protected var permissionsLauncher: ActivityResultLauncher<Array<String>>? = null

    //region providing methods
    abstract fun provideSwitchableViews(): List<View?>

    abstract fun provideAppContext(): Context

    abstract fun provideErrorHandler(): ErrorHandler<E, M>

    protected open fun provideProgressBar(): View? = null

    protected open fun providePermissions(): List<String> = emptyList()

    protected open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            context,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
    }
    //endregion

    //region fragment lifecycle methods
    /* override fun onActivityCreated(savedInstanceState: Bundle?) {
         super.onActivityCreated(savedInstanceState)
         logger.logInfo("onActivityCreated")
         hideSystemUi(activity)
     }*/

    override fun onStart() {
        super.onStart()
        logger.logInfo("onStart")
    }

    override fun onResume() {
        super.onResume()
        logger.logInfo("onResume")
    }

    override fun onPause() {
        super.onPause()
        logger.logInfo("onPause")
    }

    override fun onStop() {
        super.onStop()
        logger.logInfo("onStop")
        if (hideKeyboardOnStop)
            hideKeyboard(activity)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {

        }
    }

    //endregion

    protected open fun handleError(data: ErrorData<E, M>?) {
        logger.logInfo("try to handleError ${data != null} && !${data?.handled}")
        if (data != null && !data.handled) {
            val msg = when {
                data.throwable != null -> provideErrorHandler().getErrorMessage(data.throwable!!)
                data.model != null -> provideErrorHandler().getErrorMessage(data.model!!)
                else -> "Error"
            }
            showError(msg, data.showType, data)
            data.handled = true
        }
    }

    protected open fun showError(msg: String, showType: ShowErrorType, action: Action) {
        hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING -> enableControls()
            ShowErrorType.SHOW_ERROR_DIALOG -> {
                provideMaterialAlertDialogBuilder().setTitle(R.string.error_title)
                    .setMessage(msg)
                    .setPositiveButton(R.string.common_ok_btn) { dialogInterface, _ ->
                        dialogInterface?.dismiss()
                        handleAction(action)
                        enableControls()
                    }.create().show()
            }
            ShowErrorType.SHOW_ERROR_MESSAGE -> {
                showMessage(msg)
                handleAction(action)
                enableControls()
            }
        }
    }

    /**
     * @see IActionHandler
     */
    override fun handleAction(action: Action) {
        logger.logInfo("handleAction $action")
        when (action.name) {
            BaseActions.ACTION_FINISH -> {
                handleFinishAction(action.parameters)
            }
            else -> {
                //do nothing
            }
        }
    }

    protected open fun handleFinishAction(parameters: Bundle?){

    }

    //region helping methods

    protected open fun showMessage(text: String, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(text)
        logger.showMessage(text, duration)
    }

    protected open fun showMessage(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(provideAppContext().resources.getString(textId))
        logger.showMessage(textId, duration)
    }

    protected open fun enableControls() {
        logger.logInfo("enableControls")
        provideSwitchableViews().forEach { switchable ->
            if (switchable != null)
                switcher.enableView(switchable)
        }
    }

    protected open fun disableControls() {
        logger.logInfo("disableControls")
        provideSwitchableViews().forEach { switchable ->
            if (switchable != null)
                switcher.disableView(switchable)
        }
    }

    protected open fun hideSystemUi(activity: Activity?) {
        logger.logInfo("hideSystemUi")
        activity?.onWindowFocusChanged(true)
    }

    protected open fun hideKeyboard(activity: Activity?) {
        logger.logInfo("hideKeyboard")
        defocusAndHideKeyboard(activity)
    }

    protected open fun hideProgress() {
        logger.logInfo("hideProgress")
        provideProgressBar()?.visibility = View.INVISIBLE
    }

    //endregion
}