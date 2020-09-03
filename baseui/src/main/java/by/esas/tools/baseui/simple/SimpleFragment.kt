package by.esas.tools.baseui.simple

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.basic.BaseActivity
import by.esas.tools.baseui.mvvm.DataBindingFragment

abstract class SimpleFragment<VM : SimpleViewModel<E>, B : ViewDataBinding, E : Enum<E>> : DataBindingFragment<VM, B, E>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Settings for IObserverVM
        if (!viewModel.observerManager.checkLifecycleOwner(requireActivity())) {
            viewModel.observerManager.clearLifecycleOwner()
            viewModel.observerManager.setLifecycleOwner(provideLifecycleOwner())
            //Settings for IShowingVM
            if (!viewModel.showDialog.hasObservers()) {
                viewModel.addToObservable(viewModel.showDialog, Observer { dialog ->
                    logger.logInfo("try to showDialog ${dialog != null}")
                    dialog?.let {
                        it.show(requireFragmentManager(), dialog.TAG)
                        viewModel.showDialog.postValue(null)
                    }
                })
            }
            if (!viewModel.showBottomDialog.hasObservers()) {
                viewModel.addToObservable(viewModel.showBottomDialog, Observer { dialog ->
                    logger.logInfo("try to showBottomDialog ${dialog != null}")
                    dialog?.let {
                        it.show(requireFragmentManager(), dialog.TAG)
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
                        activity?.let {
                            if (it is BaseActivity<*>)
                                it.setLanguage(lang)
                        }
                        logger.logInfo("changeLang reset to null")
                        viewModel.changeLang.postValue(null)
                    }
                })
            }
        }
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Settings for IObserverVM
        viewModel.observerManager.clearObservables()
        //Settings for IShowingVM
        viewModel.showDialog.removeObservers(viewLifecycleOwner)
        viewModel.showDialog.postValue(null)
        viewModel.showBottomDialog.removeObservers(viewLifecycleOwner)
        viewModel.showBottomDialog.postValue(null)
        //Settings for IChangeLangVM
        viewModel.changeLang.removeObservers(viewLifecycleOwner)
        viewModel.changeLang.postValue(null)
    }
}