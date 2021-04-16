/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.basic

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.basedaggerui.inject.factory.InjectingViewModelFactory
import by.esas.tools.logger.BaseLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.util.hideSystemUI
import by.esas.tools.util.hideSystemUIR
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


abstract class BaseActivity<E : Enum<E>> : DaggerAppCompatActivity(), IBaseActivity<E> {
    open val TAG: String = "BaseActivity"

    override var logger: ILogger<E, *> = BaseLogger(TAG, null)

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(doWithAttachBaseContext(base))
    }

    override fun recreateActivity() {
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.setTag(TAG)
        logger.logInfo("onCreate")
        setFullScreen()
        hideSystemUI()
    }

    override fun onStart() {
        super.onStart()
        logger.logInfo("onStart")
    }

    override fun onStop() {
        super.onStop()
        logger.logInfo("onStop")
    }

    override fun onResume() {
        super.onResume()
        logger.logInfo("onResume")
    }

    override fun onPause() {
        super.onPause()
        logger.logInfo("onPause")
    }

    open fun setFullScreen() {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        logger.logInfo("onWindowFocusChanged $hasFocus")
        if (hasFocus)
            hideSystemUI()
    }

    protected open fun hideSystemUI() {
        logger.logInfo("hideSystemUI")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideSystemUIR(this)
        } else hideSystemUI(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
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
        return super.dispatchTouchEvent(event)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

}

