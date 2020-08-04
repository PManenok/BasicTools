package by.hgrosh.notary.base.mvvm

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import by.esas.basictools.utils.hideSystemUI
import by.hgrosh.notary.BR
import by.esas.tools.baseui.basic.BaseActivity
import by.esas.tools.baseui.mvvm.BaseViewModel
import by.hgrosh.notary.utils.logger.Logger
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject


abstract class DataBindingActivity<TViewModel : BaseViewModel, TBinding : ViewDataBinding> : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var binding: TBinding

    protected lateinit var viewModel: TViewModel

    abstract fun provideViewModel(): TViewModel
    abstract fun provideLayoutId(): Int
    open fun provideProgressBar(): ProgressBar? {
        return null
    }

    abstract fun provideLifecycleOwner(): DataBindingActivity<TViewModel, TBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.setTag(TAG)
        logger.logInfo("onCreate")
        viewModel = provideViewModel()
        viewModel.logger.setTag(viewModel.TAG)

        binding = DataBindingUtil.setContentView(this, provideLayoutId())
        binding.setVariable(BR.viewModel, viewModel)
        viewModel.alertDialogBuilder = MaterialAlertDialogBuilder(this).setCancelable(false)
        binding.lifecycleOwner = provideLifecycleOwner()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        hideSystemUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Logger.logInfo(TAG, "onWindowFocusChanged $hasFocus")
        if (hasFocus)
            hideSystemUI()
    }

    protected open fun hideSystemUI() {
        Logger.logInfo(TAG, "hideSystemUI")
        hideSystemUI(this)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    override fun onStop() {
        super.onStop()
        Logger.logWarn(TAG, "onStop")
        //setFireBaseAnalytic(null)
    }

    override fun onStart() {
        super.onStart()
        Logger.logWarn(TAG, "onStart")
        //setFireBaseAnalytic(firebaseAnalytics)
        //hideSystemUI()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is TextInputEditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    Logger.logInfo(TAG, "dispatchTouchEvent")
                    hideSystemUI()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
