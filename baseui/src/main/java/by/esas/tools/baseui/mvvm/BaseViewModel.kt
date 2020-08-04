package by.esas.tools.baseui.mvvm

import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import by.esas.tools.baseui.R
import by.esas.tools.baseui.disableView
import by.esas.tools.baseui.enableView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

abstract class BaseViewModel : ViewModel() {
    open val TAG: String = BaseViewModel::class.java.simpleName

    val progressing = ObservableBoolean(false)
    val switchableViewsList = mutableListOf<TextInputEditText?>()
    var logger: ILogger = LoggerImpl()

    var alertDialogBuilder: MaterialAlertDialogBuilder? = null
    private var alertDialog: AlertDialog? = null

    fun hideProgress() {
        progressing.set(false)
    }

    fun showProgress() {
        progressing.set(true)
    }

    override fun onCleared() {
        super.onCleared()
        logger.log("onCleared")
        switchableViewsList.clear()
        alertDialogBuilder = null
        alertDialog?.dismiss()
        alertDialog = null
    }

    /**
     * Блокируем редактирование полей.
     *
     */
    protected open fun disableControls() {
        showProgress()
        switchableViewsList.forEach { view ->
            view?.let { disableView(it) }
        }
    }

    /**
     * Возвращаем возможность редактировать поля.
     *
     */
    protected open fun enableControls() {
        hideProgress()
        switchableViewsList.forEach { view ->
            view?.let { enableView(it) }
        }
    }

    open fun handleError(error: Throwable, showMessage: Boolean = true, doOnDialogOK: () -> Unit = {}) {
        hideProgress()

        if (showMessage) {
            if (alertDialogBuilder != null) {
                alertDialog = alertDialogBuilder
                    ?.setTitle(R.string.error_title)?.setMessage(error.message)
                    ?.setPositiveButton(R.string.common_ok_btn) { _, _ ->
                        enableControls()
                        doOnDialogOK()
                    }
                    ?.show()
            } else {
                logger.showMessage(error.message)
                doOnDialogOK()
            }
        } else {
            enableControls()
        }
    }
}