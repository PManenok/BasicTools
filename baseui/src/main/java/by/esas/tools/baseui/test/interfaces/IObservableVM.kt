/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import by.esas.tools.baseui.test.ObserverManager

interface IObservableVM {
    var observerManager: ObserverManager

    fun <T> addToObservable(observable: MutableLiveData<T>, observer: Observer<T>) {
        observerManager.addObserver(observable, observer)
    }
    fun <T> addToObservable(observable: LiveData<T>, observer: Observer<T>) {
        observerManager.addObserver(observable, observer)
    }
}