/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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

/**
 * Base dialog fragment with custom state callback, disabling and enabling functions, showing and hiding progress
 * Fragment use [androidx.fragment.app.DialogFragment.STYLE_NO_TITLE] by default and is matching parent width and wrap content height
 *
 * To make dialog full screen override [layoutSettings] method and set both params in [android.view.Window.setLayout] as [ViewGroup.LayoutParams.MATCH_PARENT]
 * and don't forget to set [androidx.fragment.app.DialogFragment.STYLE_NO_FRAME] style in the [styleSettings] method to remove extra padding
 * */
abstract class BaseDialogFragment : DialogFragment() {

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
     * */
    abstract fun provideValidationList(): List<Checking>

    /**
     * Provides progress bar view which should be showed or hidden
     * @see showProgress
     * @see hideProgress
     * */
    abstract fun provideProgressBar(): View?

    //region lifecycle

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
     * Override parent onCreate method
     * Explicitly sets logger tag and invoke [styleSettings] method
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.setTag(TAG)
        logger.logInfo("onCreate")
        styleSettings()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logger.logInfo("onCreateView")
        return inflater.inflate(provideLayoutId(), container, false)
    }

    /**
     * Override parent onStart method
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

    //endregion lifecycle

    /**
     * This method prepares bundle for sending result back to fragment manager where this dialog was showed.
     * [resultBundle] should be already set before [onDismiss] was called, otherwise result will be lost.
     * Also be careful to set [DIALOG_USER_ACTION] parameter into [resultBundle], if it would not be set,
     * it will automatically clear resultBundle and set DIALOG_USER_ACTION to [DISMISS_DIALOG] value.
     * */
    protected open fun setDismissResult() {
        logger.logOrder("setDismissResult")
        val userAction: String = if (resultBundle.containsKey(DIALOG_USER_ACTION)) {
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

    /**
     * When you use this method be sure that it is called before dialog is sent to showing method in view model
     * otherwise you will need to additionally set fragment result listener with according request key in activity or fragment
     * */
    open fun setRequestKey(requestKey: String) {
        logger.logOrder("setRequestKey $requestKey")
        dialogRequestKey = requestKey
    }

    /**
     * When you use this method be sure that it is called before dialog is sent to showing method in view model
     * otherwise you will need to additionally set according request
     * */
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
     * Set dialog style here, for most cases [androidx.fragment.app.DialogFragment.STYLE_NO_TITLE] is enough
     * STYLE_NO_TITLE handle situations in old android versions where dialogs has own Title by default
     * */
    protected open fun styleSettings() {
        setStyle(STYLE_NO_TITLE, 0)
    }

    /**
     * Set dialog layout here
     * By default its width set to [ViewGroup.LayoutParams.MATCH_PARENT] and height to [ViewGroup.LayoutParams.WRAP_CONTENT]
     * */
    protected open fun layoutSettings() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * Method makes all views provided by [provideSwitchableList] method disabled which means that they ignore interaction
     * @see SwitchManager [switcher] is responsible for views disabling, so it can handle disable action for each view class type in different way
     * For example: button clickable flag can be set to false
     * Also, this method shows progress, so it become visible to user
     * This method can be used for operations which require input fields to be static and/or requires more time for execution.
     * Should be used in pair with [enableControls] method, otherwise all views would be blocked until enableControls invocation
     * */
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


