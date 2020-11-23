package by.esas.tools.baseui.simple

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.mvvm.DataBindingActivity
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment

abstract class SimpleActivity<VM : SimpleViewModel<E>, B : ViewDataBinding, E : Enum<E>> : DataBindingActivity<VM, B, E>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Settings for IObserverVM
        resetObserverManager()
        //Settings for IShowingVM
        observeShowDialog()
        observeShowBottomDialog()
        //Settings for IChangeLangVM
        observeChangeLanguage()
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    protected open fun resetObserverManager() {
        if (!viewModel.observerManager.checkLifecycleOwner(this)) {
            viewModel.observerManager.clearLifecycleOwner()
            viewModel.observerManager.setLifecycleOwner(provideLifecycleOwner())
        }
    }

    protected open fun observeShowDialog() {
        if (!viewModel.showDialog.hasObservers()) {
            viewModel.addToObservable(viewModel.showDialog, Observer { dialog ->
                onShowDialog(dialog)
            })
        }
    }

    protected open fun observeShowBottomDialog() {
        if (!viewModel.showBottomDialog.hasObservers()) {
            viewModel.addToObservable(viewModel.showBottomDialog, Observer { dialog ->
                onShowBottomDialog(dialog)
            })
        }
    }

    protected open fun observeChangeLanguage() {
        if (!viewModel.changeLang.hasObservers()) {
            viewModel.addToObservable(viewModel.changeLang, Observer { lang ->
                onChangeLanguage(lang)
            })
        }
    }

    protected open fun onShowDialog(dialog: BaseDialogFragment<*, *>?) {
        logger.logInfo("try to showDialog ${dialog != null}")
        dialog?.let {
            it.show(supportFragmentManager, dialog.TAG)
            viewModel.showDialog.postValue(null)
        }
    }

    protected open fun onShowBottomDialog(dialog: BaseBottomDialogFragment<*, *>?) {
        logger.logInfo("try to showBottomDialog ${dialog != null}")
        dialog?.let {
            it.show(supportFragmentManager, dialog.TAG)
            viewModel.showBottomDialog.postValue(null)
        }
    }

    protected open fun onChangeLanguage(lang: String?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            this.setLanguage(lang)
            logger.logInfo("changeLang reset to null")
            viewModel.changeLang.postValue(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Settings for IObserverVM
        viewModel.observerManager.clearObservables()
        //Settings for IShowingVM
        viewModel.showDialog.postValue(null)
        viewModel.showDialog.removeObservers(this)
        viewModel.showBottomDialog.postValue(null)
        viewModel.showBottomDialog.removeObservers(this)
        //Settings for IChangeLangVM
        viewModel.changeLang.postValue(null)
        viewModel.changeLang.removeObservers(this)
    }
}