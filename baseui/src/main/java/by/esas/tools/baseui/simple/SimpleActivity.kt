package by.esas.tools.baseui.simple

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.mvvm.DataBindingActivity

abstract class SimpleActivity<VM : SimpleViewModel<E>, B : ViewDataBinding, E : Enum<E>> : DataBindingActivity<VM, B, E>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Settings for IObserverVM
        viewModel.observerManager.clearLifecycleOwner()
        viewModel.observerManager.setLifecycleOwner(provideLifecycleOwner())
        //Settings for IShowingVM
        if (!viewModel.showDialog.hasObservers()) {
            viewModel.addToObservable(viewModel.showDialog, Observer { dialog ->
                logger.logInfo("try to showDialog ${dialog != null}")
                dialog?.let {
                    it.show(supportFragmentManager, dialog.TAG)
                    viewModel.showDialog.postValue(null)
                }
            })
        }
        if (!viewModel.showBottomDialog.hasObservers()) {
            viewModel.addToObservable(viewModel.showBottomDialog, Observer { dialog ->
                logger.logInfo("try to showBottomDialog ${dialog != null}")
                dialog?.let {
                    it.show(supportFragmentManager, dialog.TAG)
                    viewModel.showBottomDialog.postValue(null)
                }
            })
        }
        //Settings for IChangeLangVM
        if (!viewModel.changeLang.hasObservers()) {
            viewModel.addToObservable(viewModel.changeLang, Observer { lang ->
                logger.logInfo("try to changeLang $lang")
                if (!lang.isNullOrBlank()) {
                    logger.logInfo("changeLang to $lang")
                    this.setLanguage(lang)
                    logger.logInfo("changeLang reset to null")
                    viewModel.changeLang.postValue(null)
                }
            })
        }
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Settings for IObserverVM
        viewModel.observerManager.clearObservables()
        //Settings for IShowingVM
        viewModel.showDialog.postValue(null)
        viewModel.showBottomDialog.postValue(null)
        //Settings for IChangeLangVM
        viewModel.changeLang.postValue(null)
    }
}