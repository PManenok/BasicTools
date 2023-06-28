package by.esas.tools.screens

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.app_domain.usecase.AddCaseStatusUseCase
import by.esas.tools.base.AppVM
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.entity.TestStatusEnum
import by.esas.tools.logger.Action
import javax.inject.Inject

class MainVM @Inject constructor(
    private val addCaseStatus: AddCaseStatusUseCase
) : AppVM() {

    companion object {

        const val CASE_STATUS_DIALOG = "CASE_STATUS_DIALOG"
        const val CLEAR_CASES_TEST_DATA_DIALOG = "CLEAR_CASES_TEST_DATA_DIALOG"
        const val DIALOG_KEY = "DIALOG_KEY"
    }

    override fun handleAction(action: Action?): Boolean {
        val dialogKey = action?.parameters?.getString(DIALOG_KEY)
        val actionName = action?.name
        if (dialogKey == CASE_STATUS_DIALOG && actionName == MessageDialog.USER_ACTION_ITEM_PICKED) {
            getStatusAndUpdate(action.parameters)
        } else
            return super.handleAction(action)

        return true
    }

    val updateMenuLive = MutableLiveData<Boolean>()

    val hasBackBtn = ObservableBoolean(false)
    val hasSettingsBtn = ObservableBoolean(true)
    val title = ObservableField(App.appContext.resources.getString(R.string.menu))
    var currentCaseId = -1

    fun openCaseStatusDialog(caseLabel: String) {
        val dialog = MessageDialog()
        dialog.setRequestKey(CASE_STATUS_DIALOG)
        dialog.setNegativeButton(R.string.cancel)
        dialog.setTitle(R.string.case_status_dialog_title)
        dialog.setMessage(
            App.appContext.getString(R.string.case_status_dialog_message)
                .format(caseLabel)
        )
        dialog.setItems(
            listOf(
                makeItemInfo(TestStatusEnum.CHECKED, R.string.case_status_checked),
                makeItemInfo(TestStatusEnum.FAILED, R.string.case_status_failed),
                makeItemInfo(TestStatusEnum.IN_PROCESS, R.string.case_status_in_process),
                makeItemInfo(TestStatusEnum.UNCHECKED, R.string.case_status_unchecked)
            )
        )

        showDialog(dialog)
    }

    fun openClearCasesTestDataDialog() {
        val dialog = MessageDialog()
        dialog.setRequestKey(CLEAR_CASES_TEST_DATA_DIALOG)
        dialog.setPositiveButton(R.string.clear)
        dialog.setNegativeButton(R.string.cancel)
        dialog.setTitle(R.string.case_clear_all_dialog_title)
        dialog.setMessage(R.string.case_clear_all_dialog_message)

        showDialog(dialog)
    }

    private fun makeItemInfo(status: TestStatusEnum, resId: Int): MessageDialog.ItemInfo {
        return MessageDialog.ItemInfo(status.name, App.appContext.getString(resId))
    }

    private fun getStatusAndUpdate(parameters: Bundle?) {
        val code = parameters?.getString(MessageDialog.ITEM_CODE) ?: ""
        val status = try {
            TestStatusEnum.valueOf(code)
        } catch (e: IllegalArgumentException) {
            TestStatusEnum.UNCHECKED
        }
        //REMEMBER to execute usecase only in case that status is new to this case
        addCaseStatus.caseId = currentCaseId
        addCaseStatus.caseStatus = status
        addCaseStatus.execute {
            onComplete {
                logger.showMessage(R.string.case_status_is_changed)
                updateMenuLive.postValue(true)
            }
            onError { handleError(it) }
        }
    }
}
