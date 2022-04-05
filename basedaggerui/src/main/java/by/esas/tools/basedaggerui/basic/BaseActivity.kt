/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.basic

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import by.esas.tools.basedaggerui.R
import by.esas.tools.basedaggerui.inject.factory.InjectingViewModelFactory
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.BaseLoggerImpl
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorAction
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util.SwitchManager
import by.esas.tools.util.defocusAndHideKeyboard
import by.esas.tools.util.hideSystemUI
import by.esas.tools.util.hideSystemUIR
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<E : Enum<E>, M : BaseErrorModel<E>> : DaggerAppCompatActivity(), IChangeAppLanguage<E> {
    companion object {
        val TAG: String = BaseActivity::class.java.simpleName
    }

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory
    open val logger: ILogger<E, M> = BaseLoggerImpl(TAG, null)
    protected open var switcher: SwitchManager = SwitchManager()
    protected open var hideSystemUiOnFocus: Boolean = true

    abstract fun provideSwitchableViews(): List<View?>

    abstract fun provideErrorHandler(): ErrorHandler<E, M>

    protected open fun provideProgressBar(): View? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(doWithAttachBaseContext(base))
    }

    //region activity lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.setTag(TAG)
        logger.logInfo("onCreate")
        hideSystemUI()
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

    //region IChangeAppLanguage implementation

    /**
     * @see IChangeAppLanguage
     */
    override fun recreateActivity() {
        recreate()
    }

    /**
     * @see IChangeAppLanguage
     */
    override fun provideLogger(): ILogger<E, *> {
        return logger
    }

    //endregion IChangeAppLanguage implementation

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            this,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
    }

    open fun handleError(action: ErrorAction<E, M>?) {
        logger.logOrder("handleError ${action != null} && !${action?.handled}")
        if (action != null && !action.handled) {
            //set action as handled immediatelly after taking it to work so it wont be handled twice.
            action.handled = true
            //get error message depending on what error data we have. throwable has priority
            val msg = when {
                action.throwable != null -> provideErrorHandler().getErrorMessage(action.throwable!!)
                action.model != null -> provideErrorHandler().getErrorMessage(action.model!!)
                else -> "Error"
            }

            showError(msg, action.showType, action.getSubAction())
        }
    }

    protected open fun showError(msg: String, showType: String, action: Action?) {
        logger.logOrder("showError msg = $msg showType = $showType action = $action")
        hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING.name -> enableControls()
            ShowErrorType.SHOW_ERROR_DIALOG.name -> {
                provideMaterialAlertDialogBuilder().setTitle(R.string.error_title)
                    .setMessage(msg)
                    .setPositiveButton(R.string.common_ok_btn) { dialogInterface, _ ->
                        dialogInterface?.dismiss()
                        if (action != null)
                            handleAction(action)
                        else
                            enableControls() // default behavior is to enable controls
                    }.create().show()
            }
            ShowErrorType.SHOW_ERROR_MESSAGE.name -> {
                showMessage(msg)
                if (action != null)
                    handleAction(action)
                enableControls()
            }
        }
    }

    /**
     * Method handles action
     * @return Boolean (false in case if action was not handled by this method and true if it was handled)
     */
    open fun handleAction(action: Action): Boolean {
        logger.logOrder("handleAction $action")
        when (action.name) {
            Action.ACTION_FINISH -> {
                handleFinishAction(action.parameters)
                action.handled = true
            }
            Action.ACTION_ENABLE_CONTROLS -> {
                enableControls()
                hideProgress()
                action.handled = true
            }
            Action.ACTION_DISABLE_CONTROLS -> {
                showProgress()
                disableControls()
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

    protected open fun handleFinishAction(parameters: Bundle?) {
        finish()
    }

    //region helping methods

    open fun showMessage(text: String, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(text)
        logger.showMessage(text, duration)
    }

    open fun showMessage(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(getAppContext().resources.getString(textId))
        logger.showMessage(textId, duration)
    }

    protected open fun enableControls() {
        logger.logOrder("enableControls")
        provideSwitchableViews().forEach { switchable ->
            if (switchable != null)
                switcher.enableView(switchable)
        }
    }

    protected open fun disableControls() {
        logger.logOrder("disableControls")
        provideSwitchableViews().forEach { switchable ->
            if (switchable != null)
                switcher.disableView(switchable)
        }
    }

    protected open fun hideSystemUi(activity: Activity?) {
        logger.logOrder("hideSystemUi")
        activity?.onWindowFocusChanged(true)
    }

    protected open fun hideKeyboard(activity: Activity?) {
        logger.logOrder("hideKeyboard")
        defocusAndHideKeyboard(activity)
    }

    protected open fun hideProgress() {
        logger.logOrder("hideProgress")
        provideProgressBar()?.visibility = View.INVISIBLE
    }

    protected open fun showProgress() {
        logger.logOrder("hideProgressshowProgress")
        provideProgressBar()?.visibility = View.VISIBLE
    }

    protected open fun hideSystemUI() {
        logger.logOrder("hideSystemUI")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideSystemUIR(this)
        } else {
            hideSystemUI(this)
        }
    }

    //endregion  helping methods
}

