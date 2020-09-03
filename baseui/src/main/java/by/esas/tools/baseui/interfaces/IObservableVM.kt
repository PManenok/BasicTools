package by.esas.tools.baseui.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import by.esas.tools.baseui.ObserverManager

interface IObservableVM {
    var observerManager: ObserverManager

    fun <T> addToObservable(observable: MutableLiveData<T>, observer: Observer<T>) {
        observerManager.addObserver(observable, observer)
    }
    fun <T> addToObservable(observable: LiveData<T>, observer: Observer<T>) {
        observerManager.addObserver(observable, observer)
    }
}