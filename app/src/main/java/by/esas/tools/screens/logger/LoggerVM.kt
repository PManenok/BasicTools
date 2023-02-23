package by.esas.tools.screens.logger

import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import by.esas.tools.base.AppVM
import by.esas.tools.checker.Checker
import by.esas.tools.entity.LogItem
import by.esas.tools.logger.ILogger
import by.esas.tools.screens.logger.recycler.LogItemAdapter
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking
import by.esas.tools.utils.logger.ErrorModel
import javax.inject.Inject

class LoggerVM @Inject constructor() : AppVM() {

    val message = ObservableField<String>()
    val tag = ObservableField<String>()
    val tagMessage = ObservableField<String>()
    val category = ObservableField<String>()
    val categoryTag = ObservableField<String>()
    val categoryMessage = ObservableField<String>()
    val toastMessage = ObservableField<String>()

    val logVMAdapter = LogItemAdapter()
    val needScrolling = MutableLiveData<Boolean>()

    override var logger: ILogger<*> = object : ILogger<ErrorModel> {
        override var currentTag = "CaseLoggerVMImpl"

        override fun log(tag: String, msg: String, level: Int) {
            setLog(LogItem(tag = tag, message = msg))
        }

        override fun logCategory(category: String, tag: String, msg: String) {
            setLog(LogItem(category = category, tag = tag, message = msg))
        }

        override fun logError(throwable: Throwable) {
            setLog(LogItem(category = ILogger.CATEGORY_ERROR, message = throwable.message.toString()))
        }

        override fun logError(error: ErrorModel) {
            setLog(LogItem(category = ILogger.CATEGORY_ERROR, message = error.statusEnum))
        }

        override fun sendLogs(msg: String) {
            setLog(LogItem(message = msg))
        }

        override fun showMessage(msg: String, length: Int) {
            Toast.makeText(App.appContext, msg, length).show()
        }

        override fun showMessage(msgRes: Int, length: Int) {
            Toast.makeText(App.appContext, App.appContext.getString(msgRes), length).show()
        }
    }

    fun logTag(checks: List<FieldChecking>) {
        AppChecker().setListener(object : Checker.CheckListener {
            override fun onSuccess() {
                logger.log(tag.get().toString(), tagMessage.get().toString())
            }
        }).validate(checks)
    }

    fun logCategory(checks: List<FieldChecking>) {
        AppChecker().setListener(object : Checker.CheckListener {
            override fun onSuccess() {
                logger.logCategory(
                    category.get().toString(),
                    categoryTag.get().toString(),
                    categoryMessage.get().toString()
                )
            }
        }).validate(checks)
    }

    fun logError() {
        handleError(ErrorModel(0, AppErrorStatusEnum.UNKNOWN_ERROR))
    }

    fun showMessage(checks: List<FieldChecking>) {
        AppChecker().setListener(object : Checker.CheckListener {
            override fun onSuccess() {
                logger.showMessage(toastMessage.get().toString(), Toast.LENGTH_SHORT)
            }
        }).validate(checks)
    }

    private fun setLog(log: LogItem) {
        logVMAdapter.addItem(log)
        needScrolling.value = true
    }
}
