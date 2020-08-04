package by.esas.tools.baseui.mvvm

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import by.esas.basictools.utils.defocusAndHideKeyboard
import by.esas.tools.baseui.R
import by.hgrosh.notary.BR
import by.hgrosh.notary.R
import by.hgrosh.notary.app.App
import by.esas.tools.baseui.basic.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

abstract class DataBindingFragment<VM : BaseViewModel, B : ViewDataBinding> :
    BaseFragment<VM, B>() {
    abstract fun provideViewModel(): VM

    abstract fun provideLayoutId(): Int

    abstract fun provideTextInputETViewList(): List<TextInputEditText>

    abstract fun provideLifecycleOwner(): DataBindingFragment<VM, B>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.setTag(TAG)
        logger.logInfo("onCreateView")
        binding = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        viewModel = provideViewModel()
        viewModel.logger.setTag(viewModel.TAG)
        binding.setVariable(BR.viewModel, viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()
        viewModel.alertDialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.logInfo("onViewCreated")
        //viewModel.validationsList.clear()
        if (viewModel.switchableViewsList.isNotEmpty()) viewModel.switchableViewsList.clear()
        viewModel.switchableViewsList.addAll(provideTextInputETViewList())

    }

    private fun hideSystemUi(activity: Activity?) {
        logger.logInfo("hideSystemUi")
        activity?.onWindowFocusChanged(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logger.logInfo("onActivityCreated")
        //hideSystemUi(activity)
        //navController = findNavController(this)
    }

    override fun onStart() {
        super.onStart()
        logger.logInfo("onStart")
    }

    override fun onStop() {
        super.onStop()
        logger.logInfo("onStop")
    }

    override fun onResume() {
        super.onResume()
        logger.logInfo("onResume")
        //hideKeyboardAndNavigation(activity)

    }

    override fun onPause() {
        super.onPause()
        logger.logInfo("onPause")
        hideKeyboardAndNavigation(activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyView")
        viewModel.alertDialogBuilder = null
        //viewModel.validationsList.clear()
        viewModel.switchableViewsList.clear()
        //viewModel.navDirection.removeObservers(this)
        //viewModel.popBack.removeObservers(this)
    }

    protected fun hideKeyboardAndNavigation(activity: Activity?) {
        defocusAndHideKeyboard(activity)
        logger.logInfo("defocusAndHideKeyboard")
        //hideSystemUi(activity)
    }

    fun showMessage(text: String, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(text)
        logger.showMessage(text, duration)
    }

    fun showMessage(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
        logger.logInfo(App.appContext.resources.getString(textId))
        logger.showMessage(textId, duration)
    }

}