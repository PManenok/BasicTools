package by.esas.tools.basedaggerui.simple

import androidx.lifecycle.MutableLiveData
import by.esas.tools.basedaggerui.ObserverManager
import by.esas.tools.basedaggerui.interfaces.*
import by.esas.tools.basedaggerui.mvvm.BaseViewModel
import by.esas.tools.checker.Checking
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.domain.usecase.UseCase

abstract class SimpleViewModel<E : Enum<E>, M : BaseErrorModel<E>> : BaseViewModel<E, M>(), IShowingVM, IExecutingVM,
    ICheckingVM, IChangeLangVM {
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