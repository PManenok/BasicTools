package by.esas.tools.basedaggerui.mvvm

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import by.esas.tools.basedaggerui.basic.BaseActivity
import by.esas.tools.logger.BaseErrorModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


abstract class DataBindingActivity<TViewModel : BaseViewModel<E, *>, TBinding : ViewDataBinding, E : Enum<E>> :
    BaseActivity<E>() {

    protected lateinit var binding: TBinding

    protected lateinit var viewModel: TViewModel

    abstract fun provideViewModel(): TViewModel
    abstract fun provideLayoutId(): Int
    open fun provideProgressBar(): ProgressBar? {
        return null
    }

    abstract fun provideVariableInd(): Int

    abstract fun provideLifecycleOwner(): LifecycleOwner

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(this).setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = provideViewModel()
        viewModel.initLogger()

        binding = DataBindingUtil.setContentView(this, provideLayoutId())
        binding.setVariable(provideVariableInd(), viewModel)
        viewModel.alertDialogBuilder = provideMaterialAlertDialogBuilder()
        binding.lifecycleOwner = provideLifecycleOwner()
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
                    logger.log("dispatchTouchEvent")
                    hideSystemUI()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
