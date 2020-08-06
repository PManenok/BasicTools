package by.esas.tools.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding

abstract class BindingBottomDialogFragment<B : ViewDataBinding, E : Exception, EnumT : Enum<EnumT>>() :
    BaseBottomDialogFragment<E, EnumT>() {
    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger = provideLogger()
        logger.logInfo("onCreateView")
        binding = DataBindingUtil.inflate<B>(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableInd(), this)
        return binding.root
    }

    abstract fun provideVariableInd(): Int

    override fun provideProgressBar(): View? = null

    val showProgress = ObservableBoolean(false)

    override fun showProgress() {
        showProgress.set(true)
    }

    override fun hideProgress() {
        showProgress.set(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyView")
    }
}