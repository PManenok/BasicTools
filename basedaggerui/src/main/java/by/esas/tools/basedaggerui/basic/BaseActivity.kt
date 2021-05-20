/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.basic

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
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.BaseLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorData
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util.SwitchManager
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
    open val logger: ILogger<E, *> = BaseLogger(TAG, null)
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
    //endregion

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
    //endregion

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
    //endregion

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            this,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
    }

    protected open fun handleError(data: ErrorData<E, M>?) {
        logger.logInfo("try to handleError ${data != null} && !${data?.handled}")
        if (data != null && !data.handled) {
            val msg = when {
                data.throwable != null -> provideErrorHandler().getErrorMessage(data.throwable!!)
                data.model != null -> provideErrorHandler().getErrorMessage(data.model!!)
                else -> "Error"
            }
            showError(msg, data.showType, data.actionName)
            data.handled = true
        }
    }

   protected open fun showError(msg: String, showType: ShowErrorType, actionName: String? = null) {
        hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING -> enableControls()
            ShowErrorType.SHOW_ERROR_DIALOG -> {
                provideMaterialAlertDialogBuilder().setTitle(R.string.error_title)
                    .setMessage(msg)
                    .setPositiveButton(R.string.common_ok_btn) { dialogInterface, _ ->
                        dialogInterface?.dismiss()
                        handleAction(actionName)
                        enableControls()
                    }.create().show()
            }
            ShowErrorType.SHOW_ERROR_MESSAGE -> {
                showMessage(msg)
                handleAction(actionName)
                enableControls()
            }
        }
    }

    open fun showMessage(text: String, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(text)
        logger.showMessage(text, duration)
    }

    open fun showMessage(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(getAppContext().resources.getString(textId))
        logger.showMessage(textId, duration)
    }

    protected open fun hideProgress() {
        logger.logInfo("hideProgress")
        provideProgressBar()?.visibility = View.INVISIBLE
    }

    protected open fun handleAction(action: String?) {
        logger.logInfo("handleAction $action")
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

    protected open fun hideSystemUI() {
        logger.logInfo("hideSystemUI")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideSystemUIR(this)
        } else hideSystemUI(this)
    }
}

