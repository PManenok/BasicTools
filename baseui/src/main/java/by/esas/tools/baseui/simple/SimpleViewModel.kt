package by.esas.tools.baseui.simple

import androidx.lifecycle.MutableLiveData
import by.esas.tools.baseui.ObserverManager
import by.esas.tools.baseui.interfaces.*
import by.esas.tools.baseui.mvvm.BaseViewModel
import by.esas.tools.checker.Checking
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.domain.usecase.UseCase

abstract class SimpleViewModel<E : Enum<E>> : BaseViewModel<E>(), IShowingVM, IObservableVM, IExecutingVM, ICheckingVM, IChangeLangVM {
    override val checksList = mutableListOf<Checking>()
    override val showDialog: MutableLiveData<BaseDialogFragment<*, *>?> = MutableLiveData<BaseDialogFragment<*, *>?>()
        .apply {
            value = null
        }
    override val showBottomDialog: MutableLiveData<BaseBottomDialogFragment<*, *>?> = MutableLiveData<BaseBottomDialogFragment<*, *>?>()
        .apply {
            value = null
        }
    override var useCases: MutableList<UseCase<*, *, *>> = mutableListOf<UseCase<*, *, *>>()
    override var observerManager: ObserverManager = ObserverManager()
    override val changeLang: MutableLiveData<String?> = MutableLiveData<String?>()
        .apply {
            value = null
        }

    override fun onCleared() {
        super.onCleared()
        unsubscribeUseCases()
        clearChecks()
    }
}