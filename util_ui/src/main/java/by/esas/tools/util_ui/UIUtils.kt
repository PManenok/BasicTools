/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util_ui

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.text.method.KeyListener
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat

fun disableView(view: EditText) {
    view.setText(view.text.toString().trim())
    if (view.keyListener != null) {
        view.tag = view.keyListener
        view.keyListener = null
    }
}

fun enableView(view: EditText) {
    if (view.tag != null)
        view.keyListener = view.tag as KeyListener
}

/**
 * This method hides system UI and makes app fullscreen (screen height will be from
 * the very top to the very bottom), and system bars will hide.
 *
 * Note! That it works only from [Build.VERSION_CODES.LOLLIPOP] (Api 21, Android 5),
 * for earlier versions nothing will change.
 * */
fun hideSystemUI(activity: Activity?, isDark: Boolean) {
    // Enables regular immersive mode.
    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    activity?.window?.apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R/*30-11*/ -> {
                setDecorFitsSystemWindows(false)
                insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                if (!isDark) {
                    insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
                // Hide both the status bar and the navigation bar
                insetsController?.hide(WindowInsetsCompat.Type.systemBars())

                statusBarColor = if (isDark) Color.BLACK else Color.WHITE
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M/*23-6*/ -> {
                @Suppress("DEPRECATION")
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = if (isDark) {
                    (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                } else {
                    (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
                statusBarColor = if (isDark) Color.BLACK else Color.WHITE
            }
            else /*only from 21-5*/ -> {
                @Suppress("DEPRECATION")
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                // statusBarColor cannot be changed, because before API 23 android can't change status icons color
                statusBarColor = Color.BLACK
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun hideSystemUIR(activity: Activity?) {
    // You have to wait for the view to be attached to the
    // window (otherwise, windowInsetController will be null)
    activity?.window?.insetsController?.let { controller ->
        controller.hide(WindowInsets.Type.systemBars())
        controller.hide(WindowInsets.Type.ime())
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun hideSystemUIR(view: View) {
    // You have to wait for the view to be attached to the
    // window (otherwise, windowInsetController will be null)
    view.windowInsetsController?.let { controller ->
        controller.hide(WindowInsets.Type.systemBars())
        controller.hide(WindowInsets.Type.ime())
    }
}