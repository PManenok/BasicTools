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
import by.esas.tools.logger.BaseLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.util.SwitchManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Base bottom dialog fragment with custom state callback, disabling and enabling functions, showing and hiding progress
 * */
abstract class BaseBottomDialogFragment<E : Exception, EnumT : Enum<EnumT>> : BottomSheetDialogFragment() {
    open val TAG: String = BaseBottomDialogFragment::class.java.simpleName

    /**
     * Flag for StateCallback, which means that dialog was dismissed after some dialog actions
     * For example after another successful callback like anotherCallback.onOkClick()
     * */
    protected var afterOk: Boolean = false

    /**
     * StateCallback will call after dialog dismiss or which means that dialog was dismissed after some dialog actions
     * For example after another successful callback like anotherCallback.onOkClick()
     * */
    protected var stateCallback: StateCallback<E>? = null

    /**
     * Flag that shows if provided StateCallback should be able to enable controls in this dialog holder
     * @see StateCallbackProvider
     * */
    protected open val enablingStateCallback: Boolean = false

    /**
     * Simple logger that is used for logging and provides ability to send all logs into one place
     * depends on its interface realisation
     * By default this logger is [BaseLogger]
     * */
    protected open var logger: ILogger<EnumT, *> = BaseLogger(BaseBottomDialogFragment::class.java.simpleName, this.context)

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
     * */
    abstract fun provideValidationList(): List<Checking>

    /**
     * Provides progress bar view which should be showed or hidden
     * @see showProgress
     * @see hideProgress
     * */
    abstract fun provideProgressBar(): View?

    /**
     * Override parents onCreate method
     * Explicitly sets logger tag and invoke [styleSettings] method
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.setTag(TAG)
        logger.logInfo("onCreate")
        styleSettings()
    }

    /**
     * Override parents onCreateDialog method
     * Its purpose is to save behavior property to be able to use it from this dialog fragment methods
     * */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            behavior = dialog.behavior
        }
        return dialog
    }

    /**
     * Override parents onDismiss method and send dismiss event to [StateCallback]
     * */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        provideStateCallback()?.onDismiss(afterOk)
        logger.logInfo("onDismiss")
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

    fun setCallbackForState(callback: StateCallback<E>?) {
        this.stateCallback = callback
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
     * Provide [StateCallback]
     * If it is possible, get callback from [targetFragment][androidx.fragment.app.Fragment.getTargetFragment]
     * or from [getActivity][androidx.fragment.app.Fragment.getActivity] if one of them is instance of [StateCallbackProvider]
     * targetFragment has advantage, then goes activity, and if both aren't StateCallbackProvider instances method would provide [stateCallback]
     * which can be null
     * */
    protected open fun provideStateCallback(): StateCallback<out Exception>? {
        val frag = targetFragment
        val act = activity
        return when {
            frag is StateCallbackProvider<*> -> {
                frag.provideStateCallback(enablingStateCallback)
            }
            act is StateCallbackProvider<*> -> {
                act.provideStateCallback(enablingStateCallback)
            }
            else -> stateCallback
        }
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
        provideProgressBar()?.visibility = View.VISIBLE
    }

    /**
     * Method makes progress invisible to user
     * @see enableControls
     * */
    protected open fun hideProgress() {
        provideProgressBar()?.visibility = View.INVISIBLE
    }
}


