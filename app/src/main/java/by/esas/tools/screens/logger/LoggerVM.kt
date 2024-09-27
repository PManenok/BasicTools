package by.esas.tools.screens.logger

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
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

    override var logger: ILogger<*> = object : ILogger<ErrorModel>() {
        override fun getTag(): String = "CaseLoggerVMImpl"

        override fun logLocally(tag: String, msg: String, level: Int) {
            setLog(LogItem(tag, "", msg))
        }

        override fun logCategory(category: String, tag: String, msg: String) {
            setLog(LogItem(tag, category, msg))
        }

        override fun throwable(throwable: Throwable) {
            setLog(LogItem("", ILogger.CATEGORY_ERROR, throwable.message.toString()))
        }

        override fun errorModel(error: ErrorModel) {
            setLog(LogItem("", ILogger.CATEGORY_ERROR, error.status))
        }
    }

    fun logTag(checks: List<FieldChecking>) {
        AppChecker().setListener(object : Checker.CheckListener {
            override fun onSuccess() {
                logger.i(tag.get().toString(), tagMessage.get().toString())
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

    /*fun showMessage(checks: List<FieldChecking>) {
        AppChecker().setListener(object : Checker.CheckListener {
            override fun onSuccess() {
                logger.showMessage(toastMessage.get().toString(), Toast.LENGTH_SHORT)
            }
        }).validate(checks)
    }*/

    private fun setLog(log: LogItem) {
        logVMAdapter.addItem(log)
        needScrolling.value = true
    }
}
