/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.simple

import androidx.lifecycle.MutableLiveData
import by.esas.tools.baseui.test.ObserverManager
import by.esas.tools.baseui.test.interfaces.*
import by.esas.tools.baseui.test.mvvm.BaseViewModel
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