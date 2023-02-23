/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import by.esas.tools.checker.Checking
import by.esas.tools.dialog.Config.CANCEL_DIALOG
import by.esas.tools.dialog.Config.DIALOG_PARAMETERS_BUNDLE
import by.esas.tools.dialog.Config.DIALOG_RESULT_BUNDLE
import by.esas.tools.dialog.Config.DIALOG_USER_ACTION
import by.esas.tools.dialog.Config.DISMISS_DIALOG
import by.esas.tools.logger.BaseLoggerImpl
import by.esas.tools.logger.ILogger
import by.esas.tools.util.SwitchManager
import by.esas.tools.util.TAGk
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Base bottom dialog fragment with custom state callback, disabling and enabling functions, showing and hiding progress
 * */
abstract class BaseBottomDialogFragment : BottomSheetDialogFragment() {

    open val TAG: String = TAGk

    protected val defaultRequestKey: String = TAGk
    protected var dialogRequestKey: String = defaultRequestKey
    protected var resultBundle: Bundle = Bundle()
    protected var paramsBundle: Bundle = Bundle()

    /**
     * Simple logger that is used for logging and provides ability to send all logs into one place
     * depends on its interface realisation
     * By default this logger is [BaseLoggerImpl]
     * */
    protected open var logger: ILogger<*> = BaseLoggerImpl(TAGk, this.context)

    /**
     * Manager that provides enabling and disabling views functionality
     * @see SwitchManager
     * */
    protected open var switcher: SwitchManager = SwitchManager()

    /**
     * This parameter is set in [onCreateDialog] method
     * Can be used for mor control of bottom fragment behavior
     * @see BottomSheetBehavior
     * */
    protected var behavior: BottomSheetBehavior<FrameLayout>? = null

    /**
     * Provides layout resource id for this dialog fragment
     * */
    abstract fun provideLayoutId(): Int

    /**
     * Provides list of views that should be disabled/enabled in some cases
     * @see disableControls
     * @see enableControls
     * */
    abstract fun provideSwitchableList(): List<View?>

    /**
     * Provides list of validation checks that can be used for input check
     * @see by.esas.tools.checker.Checking
     * @see by.esas.tools.checker.Checker
     */
    abstract fun provideValidationList(): List<Checking>

    /**
     * Provides progress bar view which should be showed or hidden
     * @see showProgress
     * @see hideProgress
     **/
    abstract fun provideProgressBar(): View?

    //region lifecycle methods

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(DIALOG_PARAMETERS_BUNDLE, paramsBundle)
        outState.putBundle(DIALOG_RESULT_BUNDLE, resultBundle)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            paramsBundle = savedInstanceState.getBundle(DIALOG_PARAMETERS_BUNDLE) ?: Bundle()
            resultBundle = savedInstanceState.getBundle(DIALOG_RESULT_BUNDLE) ?: Bundle()
        }
    }

    /**
     * Override parents onCreate method
     * Explicitly sets logger tag and invoke [styleSettings] method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.setTag(TAG)
        logger.logInfo("onCreate")
        styleSettings()
    }

    /**
     * Override parents onCreateDialog method
     * Its purpose is to save behavior property to be able to use it from this dialog fragment methods
     **/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            behavior = dialog.behavior
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logger.logInfo("onCreateView")
        return inflater.inflate(provideLayoutId(), container, false)
    }

    /**
     * Override parents onStart method
     * Invoke [layoutSettings] method
     * */
    override fun onStart() {
        super.onStart()
        logger.logInfo("onStart")

        layoutSettings()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyView")
    }

    /**
     * Override parent onDismiss method and [setDismissResult]
     * */
    override fun onDismiss(dialog: DialogInterface) {
        setDismissResult()
        super.onDismiss(dialog)
        logger.logInfo("onDismiss")
    }

    /**
     * Override parent onCancel method and set [DIALOG_USER_ACTION] in [resultBundle] to [CANCEL_DIALOG]
     * */
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        resultBundle.putString(DIALOG_USER_ACTION, CANCEL_DIALOG)
        logger.logInfo("onCancel")
    }

    //endregion lifecycle methods

    /**
     * This method prepares bundle for sending result back to fragment manager where this dialog was showed.
     * [resultBundle] should be already set before [onDismiss] was called, otherwise result will be lost.
     * Also be careful to set [DIALOG_USER_ACTION] parameter into [resultBundle], if it would not be set,
     * it will automatically clear resultBundle and set DIALOG_USER_ACTION to [DISMISS_DIALOG] value.
     * */
    protected open fun setDismissResult() {
        logger.logOrder("setDismissResult")
        val userAction: String = if (resultBundle.containsKey(Config.DIALOG_USER_ACTION)) {
            resultBundle.getString(DIALOG_USER_ACTION, "")
        } else {
            resultBundle.clear()
            resultBundle.putString(DIALOG_USER_ACTION, DISMISS_DIALOG)
            DISMISS_DIALOG
        }
        logger.logInfo("userAction $userAction; result size ${resultBundle.size()}")

        val bundle = Bundle()
        bundle.putAll(resultBundle)
        bundle.putAll(paramsBundle)
        parentFragmentManager.setFragmentResult(dialogRequestKey, bundle)
    }

    open fun setRequestKey(requestKey: String) {
        logger.logOrder("setRequestKey $requestKey")
        dialogRequestKey = requestKey
    }

    open fun getRequestKey(): String {
        return dialogRequestKey
    }

    /**
     * Set bundle of parameters that dialog may use in its work and\or return
     * along with resultBundle as fragment result
     * */
    open fun setParams(bundle: Bundle) {
        logger.logOrder("setParams $bundle")
        paramsBundle = bundle
    }

    /**
     * Set dialog style here
     * By default do nothing
     * */
    protected open fun styleSettings() {
        //Do nothing
    }

    /**
     * Set dialog layout here
     * By default do nothing
     * */
    protected open fun layoutSettings() {
        //Do nothing
    }

    /**
     * Method makes all views provided by [provideSwitchableList] method disabled which means that they ignore interaction
     * @see SwitchManager [switcher] is responsible for views disabling, so it can handle disable action for each view class type in different way
     * For example: button clickable flag can be set to false
     * Also, this method shows progress, so it become visible to user
     * This method can be used for operations which require input fields to be static and/or requires more time for execution.
     * Should be used in pair with [enableControls] method, otherwise all views would be blocked until enableControls invocation
     */
    protected open fun disableControls() {
        logger.logOrder("disableControls")
        showProgress()
        provideSwitchableList().forEach { view ->
            if (view != null) switcher.disableView(view)
        }
    }

    /**
     * Method makes all views provided by [provideSwitchableList] method enabled which means
     * that they stop ignoring interaction if they were disabled previously
     * @see SwitchManager [switcher] is responsible for views enabling, so it can handle enable action for each view class type in different way
     * For example: button clickable flag can be set to true
     * Also, this method hides progress, so it become invisible to user
     * This method should be used after end of operation execution, which required invocation of [disableControls] method,
     * otherwise all views would be blocked until enableControls invocation
     * */
    protected open fun enableControls() {
        logger.logOrder("enableControls")
        hideProgress()
        provideSwitchableList().forEach { view ->
            if (view != null) switcher.enableView(view)
        }
    }

    /**
     * Method makes progress visible to user
     * @see disableControls
     * */
    protected open fun showProgress() {
        logger.logOrder("showProgress")
        provideProgressBar()?.visibility = View.VISIBLE
    }

    /**
     * Method makes progress invisible to user
     * @see enableControls
     * */
    protected open fun hideProgress() {
        logger.logOrder("hideProgress")
        provideProgressBar()?.visibility = View.INVISIBLE
    }
}


