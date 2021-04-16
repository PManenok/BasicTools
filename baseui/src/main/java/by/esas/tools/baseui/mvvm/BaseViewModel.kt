/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.mvvm

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import by.esas.tools.baseui.R
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.ILogger
import by.esas.tools.util.SwitchManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseViewModel<E : Enum<E>, M : BaseErrorModel<E>> : ViewModel() {
    open val TAG: String = BaseViewModel::class.java.simpleName

    val progressing = ObservableBoolean(false)
    val switchableViewsList = mutableListOf<View?>()

    protected open var switcher: SwitchManager = SwitchManager()
    lateinit var logger: ILogger<E, M>

    var alertDialogBuilder: MaterialAlertDialogBuilder? = null
    private var alertDialog: AlertDialog? = null

    open fun hideProgress() {
        progressing.set(false)
    }

    open fun showProgress() {
        progressing.set(true)
    }

    open fun dismissDialogs() {
        if (alertDialog?.isShowing == true) {
            alertDialog?.dismiss()
        }
        alertDialog = null
    }

    override fun onCleared() {
        super.onCleared()
        logger.log("onCleared")

        switchableViewsList.clear()
        alertDialogBuilder = null
    }

    /**
     * Блокируем редактирование полей.
     */
    protected open fun disableControls(): Boolean {
        showProgress()
        var result: Boolean = true
        switchableViewsList.forEach { view ->
            view?.let { result = switcher.disableView(it) && result }
        }
        return result
    }

    /**
     * Возвращаем возможность редактировать поля.
     */
    protected open fun enableControls(): Boolean {
        hideProgress()
        var result: Boolean = true
        switchableViewsList.forEach { view ->
            view?.let { result = switcher.enableView(it) && result }
        }
        return result
    }

    open fun handleError(error: Throwable, doOnDialogOK: () -> Unit = {}) {
        handleError(error = mapError(error), showType = ShowErrorType.SHOW_ERROR_DIALOG, doOnDialogOK = doOnDialogOK)
    }

    open fun handleError(error: Throwable, showType: ShowErrorType, doOnDialogOK: () -> Unit = {}) {
        handleError(error = mapError(error), showType = showType, doOnDialogOK = doOnDialogOK)
    }

    open fun handleError(error: M, doOnDialogOK: () -> Unit = {}) {
        handleError(error = error, showType = ShowErrorType.SHOW_ERROR_DIALOG, doOnDialogOK = doOnDialogOK)
    }

    open fun handleError(error: M, showType: ShowErrorType, doOnDialogOK: () -> Unit = {}) {
        hideProgress()
        /*if (error.statusEnum == AppErrorStatusEnum.NET_SSL_HANDSHAKE) {
            logger.logError(SSLContext.getDefault().defaultSSLParameters.protocols?.contentToString() ?: "SSL protocols: Empty")
        }*/
        logger.logError(error)
        if (showType != ShowErrorType.SHOW_NOTHING) {
            if (showType == ShowErrorType.SHOW_ERROR_DIALOG) {
                alertDialog = alertDialogBuilder?.setTitle(R.string.error_title)
                    ?.setMessage(getErrorMessage(error))
                    ?.setPositiveButton(R.string.common_ok_btn) { dialogInterface, _ ->
                        dialogInterface?.dismiss()
                        alertDialog = null
                        enableControls()
                        doOnDialogOK()
                    }?.create()
            }
            if (alertDialog != null) {
                alertDialog?.show()
            } else {
                logger.showMessage(getErrorMessage(error))
                enableControls()
                doOnDialogOK()
            }
        } else {
            enableControls()
        }
    }

    abstract fun getErrorMessage(error: M): String

    protected abstract fun mapError(e: Throwable): M

    abstract fun initLogger()

    enum class ShowErrorType {
        SHOW_ERROR_DIALOG,
        SHOW_ERROR_MESSAGE,
        SHOW_NOTHING
    }
}