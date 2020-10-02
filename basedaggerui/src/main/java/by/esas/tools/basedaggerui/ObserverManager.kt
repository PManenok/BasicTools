package by.esas.tools.basedaggerui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.ref.WeakReference

class ObserverManager {
    private var lifecycleOwner: WeakReference<LifecycleOwner>? = null
    private val listObservables: MutableList<LiveData<*>> = mutableListOf()

    fun checkLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        return this.lifecycleOwner?.get() == lifecycleOwner
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = WeakReference(lifecycleOwner)
    }

    fun <T> addObserver(observable: LiveData<T>, observer: Observer<T>) {
        lifecycleOwner?.get()?.let {
            observable.observe(it, observer)
            listObservables.add(observable)
        }
    }

    fun <T> addObserver(observable: MutableLiveData<T>, observer: Observer<T>) {
        lifecycleOwner?.get()?.let {
            observable.observe(it, observer)
            listObservables.add(observable)
        }
    }

    fun clearObservables() {
        lifecycleOwner?.get()?.let { owner ->
            listObservables.forEach { observer ->
                observer.removeObservers(owner)
            }
            listObservables.clear()
        }
    }

    fun <T> clearObservable(observable: LiveData<T>) {
        lifecycleOwner?.get()?.let { owner ->
            if (listObservables.contains(observable)) {
                listObservables.remove(observable)
                observable.removeObservers(owner)
            }
        }
    }

    fun clearLifecycleOwner() {
        clearObservables()
        lifecycleOwner?.clear()
    }
}