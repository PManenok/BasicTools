package by.esas.tools

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import by.esas.tools.dialog.GetPasswordDialog
import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.IErrorMapper
import javax.inject.Inject

class MainVM @Inject constructor(val mapper: AppErrorMapper) : AppVM() {

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

    fun onPhoneCodeClick() {
        showDialog(GetPasswordDialog())
    }
}

