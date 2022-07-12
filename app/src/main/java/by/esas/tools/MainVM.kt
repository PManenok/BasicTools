package by.esas.tools

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import by.esas.tools.dialog.GetPasswordDialog
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.logger.Action
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.usecase.GetDefaultCardUseCase
import javax.inject.Inject

class MainVM @Inject constructor(
    val mapper: AppErrorMapper,
    val useCase: GetDefaultCardUseCase
) : AppVM() {

    override val TAG: String = "MainVM"
    override fun provideMapper(): IErrorMapper<ErrorModel> {
        return mapper
    }

    var update: () -> Unit = {}

    fun setUp() {
        serviceName.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    mainText.postValue(serviceName.get() ?: "")
                }
            }
        )
    }

    val serviceName = ObservableField<String>("")
    val showAdditional = ObservableBoolean(false)
    val mainText = MutableLiveData<String>().apply { postValue("") }

    val password = MutableLiveData<String>("")

    fun onAdditionalClick() {
        showAdditional.set(!showAdditional.get())
        update()
    }

    fun testError() {
        disableControls()
        useCase.execute {
            onComplete {
                enableControls()
            }
            onError {
                handleError(error = it, actionName = Action.ACTION_ENABLE_CONTROLS)
            }
        }
    }

    fun onPhoneCodeClick() {
        showDialog(GetPasswordDialog())
    }

    fun onLabelClick() {
        disableControls()
        val dialog = MessageDialog(true)
        dialog.setEnableControlsOnDismiss(true)
        dialog.setTitle("Title")
        dialog.setMessage("Message")
        dialog.setPositiveButton("OK", "positiveAction")
        dialog.setItems(listOf("one", "two", "three"), "itemActionName")
        showDialog(dialog)
    }
}

