/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.basic

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import by.esas.tools.baseui.R
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.BaseLoggerImpl
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorAction
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util.SwitchManager
import by.esas.tools.util.TAGk
import by.esas.tools.util.configs.IChangeSettings
import by.esas.tools.util.defocusAndHideKeyboard
import by.esas.tools.util.hideSystemUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Base class for activity that can:
 * - update base context on [attachBaseContext] method
 * - hide system UI when input focus was lost (also hides keyboard)
 * - show [by.esas.tools.dialog.BaseDialogFragment] and [by.esas.tools.dialog.BaseBottomDialogFragment]
 * - [handleAction] like: [ErrorAction.ACTION_ERROR], [Action.ACTION_FINISH], [Action.ACTION_ENABLE_CONTROLS],
 *   [Action.ACTION_DISABLE_CONTROLS], [Action.ACTION_HIDE_KEYBOARD]
 * - [ErrorAction] is handled via [handleError] method, and it use [ShowErrorType]
 *   to choose how to show error to user
 */
abstract class BaseActivity<M : BaseErrorModel> : AppCompatActivity(), IChangeSettings {

    open val logger: ILogger<*> = BaseLoggerImpl(TAGk, null)
    protected open var switcher: SwitchManager = SwitchManager()
    protected open var hideSystemUiOnFocus: Boolean = true

    abstract fun provideLayoutId(): Int

    abstract fun provideSwitchableViews(): List<View?>

    abstract fun provideErrorHandler(): ErrorHandler<M>

    /**
     * Do not forget to use super call for this method
     * */
    abstract fun provideRequestKeys(): List<String>

    /**
     * You should call super methods for this method to get result listeners from parent classes
     * */
    abstract fun provideFragmentResultListener(requestKey: String): FragmentResultListener?

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(doWithAttachBaseContext(base))
    }

    //region activity lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.setTag(TAGk)
        logger.logInfo("onCreate")
        setupRootView()
        val contentView = window.decorView.findViewById<View>(android.R.id.content) as ViewGroup

        handleStatusBar(contentView)
        hideSystemUI()
        setupFragmentResultListeners(provideRequestKeys())
    }

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
    }

    //endregion activity lifecycle methods

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        logger.logInfo("onWindowFocusChanged hasFocus = $hasFocus; hideSystemUiOnFocus = $hideSystemUiOnFocus")
        if (hideSystemUiOnFocus && hasFocus)
            hideSystemUI()
    }

    //region show dialog functionality

    open fun setupFragmentResultListeners(requestKeys: List<String>) {
        requestKeys.forEach { key ->
            provideFragmentResultListener(key)?.let { listener ->
                supportFragmentManager.setFragmentResultListener(key, this, listener)
            }
        }
    }

    protected open fun onShowDialog(dialog: DialogFragment, tag: String) {
        logger.logInfo("try to showDialog $tag")
        val prevWithSameTag: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (prevWithSameTag != null && prevWithSameTag is BaseDialogFragment
            && prevWithSameTag.getDialog() != null && prevWithSameTag.getDialog()?.isShowing == true
            && !prevWithSameTag.isRemoving
        ) {
            //there is currently showed dialog fragment with same TAG
        } else {
            //there is no currently showed dialog fragment with same TAG
            dialog.show(supportFragmentManager, tag)
        }
    }

    //endregion show dialog functionality

    //region touch event and keyboard

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        handleTouchOutOfInputField(event)
        return super.dispatchTouchEvent(event)
    }

    protected open fun handleTouchOutOfInputField(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    logger.logInfo("dispatchTouchEvent")
                    hideSystemUI()
                }
            }
        }
    }

    //endregion touch event and keyboard

    //region IChangeSettings implementation

    /**
     * @see IChangeSettings
     */
    override fun recreateActivity() {
        recreate()
    }

    /**
     * @see IChangeSettings
     */
    override fun logInfo(info: String) {
        logger.logInfo(info)
    }

    //endregion IChangeSettings implementation

    //region action

    /**
     * Method handles action
     * @return Boolean (false in case if action was not handled by this method and true if it was handled)
     */
    open fun handleAction(action: Action): Boolean {
        logger.logOrder("handleAction $action")
        when (action.name) {
            ErrorAction.ACTION_ERROR -> {
                // "as?" - is a safe cast that will not throw an exception, so we can use suppress warning in this case
                @Suppress("UNCHECKED_CAST")
                handleError(action as? ErrorAction<M>)
            }
            Action.ACTION_FINISH -> {
                handleFinishAction(action.parameters)
                action.handled = true
            }
            Action.ACTION_HIDE_KEYBOARD -> {
                hideKeyboard(this)
                action.handled = true
            }
            else -> {
                // return false in case if action was not handled by this method
                return false
            }
        }
        return true
    }

    open fun handleError(action: ErrorAction<M>?) {
        logger.logOrder("handleError ${action != null} && !${action?.handled}")
        if (action != null && !action.handled) {
            //set action as handled immediately after taking it to work so it wont be handled twice.
            action.handled = true
            //get error message depending on what error data we have. throwable has priority
            val msg = when {
                action.model != null -> provideErrorHandler().getErrorMessage(action.model!!)
                else -> "Error"
            }

            showError(msg, action.getShowType(), action.getSubAction())
        }
    }

    protected open fun showError(msg: String, showType: String, action: Action?) {
        logger.logOrder("showError msg = $msg showType = $showType action = $action")
        hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING.name -> switchControlsOn()
            ShowErrorType.SHOW_ERROR_DIALOG.name -> showErrorDialog(msg, action)
            ShowErrorType.SHOW_ERROR_MESSAGE.name -> showErrorMessage(msg, action)
        }
    }

    protected open fun showErrorDialog(msg: String, action: Action?) {
        provideMaterialAlertDialogBuilder().setTitle(R.string.base_ui_error_title)
            .setMessage(msg)
            .setPositiveButton(R.string.base_ui_common_ok_btn) { dialogInterface, _ ->
                dialogInterface?.dismiss()
                if (action != null) {
                    handleAction(action)
                } else {
                    // default behavior is to enable controls
                    switchControlsOn()
                }
            }.create().show()
    }

    protected open fun showErrorMessage(msg: String, action: Action?) {
        showMessage(msg)
        if (action != null) {
            handleAction(action)
        } else {
            // default behavior is to enable controls
            switchControlsOn()
        }
    }

    protected open fun handleFinishAction(parameters: Bundle?) {
        finish()
    }

    //endregion action

    //region helping methods

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            this,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
    }

    open fun showMessage(text: String, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(text)
        logger.showMessage(text, duration)
    }

    open fun showMessage(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(getAppContext().resources.getString(textId))
        logger.showMessage(textId, duration)
    }

    protected open fun hideKeyboard(activity: Activity?) {
        logger.logOrder("hideKeyboard")
        defocusAndHideKeyboard(activity)
    }

    protected open fun hideSystemUI() {
        logger.logOrder("hideSystemUI")
        if (window?.decorView != null) {
            val isDark = resources.getBoolean(R.bool.is_dark)
            logger.logInfo("hideSystemUI decorView != null; isDark = $isDark")
            hideSystemUI(this, isDark)
        }
    }

    @Suppress("DEPRECATION")
    open fun handleStatusBar(root: View) {
        logger.logOrder("handleStatusBar")
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val statusInset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val statusBar = statusInset.top.takeIf { it != 0 } ?: insets.systemWindowInsetTop
            logger.logInfo("insets statusBar = $statusBar")
            root.updatePadding(top = statusBar)
            return@setOnApplyWindowInsetsListener insets
        }
    }

    /**
     * Override this method if you have view which is indicator of some screen progress
     * and you need to hide it (like progress bar)
     */
    protected open fun hideProgress() {
        logger.logInfo("hideProgress")
    }

    /**
     * Override this method if you have view which is indicator of some screen progress
     * and you need to show it (like progress bar)
     */
    protected open fun showProgress() {
        logger.logInfo("showProgress")
    }

    /**
     * Use this method only to make control views on the screen interactive
     * (like buttons, input fields, switchers, checkboxes and etc.)
     */
    protected open fun switchControlsOn() {
        logger.logInfo("switchControlsOn")
        provideSwitchableViews().forEach { switchable ->
            if (switchable != null) switcher.enableView(switchable)
        }
    }

    /**
     * Use this method only to make control views on the screen non interactive
     * (like buttons, input fields, switchers, checkboxes and etc.)
     */
    protected open fun switchControlsOff() {
        logger.logInfo("switchControlsOff")
        provideSwitchableViews().forEach { switchable ->
            if (switchable != null) switcher.disableView(switchable)
        }
    }

    //endregion  helping methods

    //region settings methods
    open fun setupRootView() {
        setContentView(provideLayoutId())
    }
    //endregion settings methods
}

