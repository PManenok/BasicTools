package by.esas.tools.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.esas.tools.logger.BaseLogger
import by.esas.tools.logger.ILogger
import by.esas.tools.util.disableView
import by.esas.tools.util.enableView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

abstract class BaseBottomDialogFragment<E : Exception, EnumT : Enum<EnumT>>() : BottomSheetDialogFragment() {
    abstract val TAG: String
    protected open lateinit var logger: ILogger<EnumT>
    //protected val validationList: MutableList<Validation> = mutableListOf()
    protected var switchableViewsList: List<TextInputEditText?> = emptyList()
    protected var stateCallback: StateCallback<E>? = null
    protected var afterOk: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NO_FRAME, R.style.AppTheme)
    }

    abstract fun provideLayoutId(): Int
    abstract fun provideSwitchableList(): List<TextInputEditText>
    //abstract fun provideValidationList(): List<Validation>
    abstract fun provideProgressBar(): View?
    protected open fun provideLogger(): ILogger<EnumT> {
        return BaseLogger(TAG, this.context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logger = provideLogger()
        logger.logInfo("onCreateView")
        return inflater.inflate(provideLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchableViewsList = provideSwitchableList()
        //todo uncomment
        //validationList.clear()
        //validationList.addAll(provideValidationList())
    }

    protected open fun disableControls() {
        showProgress()
        switchableViewsList.forEach { view ->
            if (view != null) disableView(view)
        }
    }

    /**
     * Возвращаем возможность редактировать поля.
     *
     */
    protected open fun enableControls() {
        hideProgress()
        switchableViewsList.forEach { view ->
            if (view != null) enableView(view)
        }
    }

    protected open fun showProgress() {
        provideProgressBar()?.visibility = View.VISIBLE
    }

    protected open fun hideProgress() {
        provideProgressBar()?.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        //dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        stateCallback?.onDismiss(afterOk)
        logger.logInfo("onDialogDismiss")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyDialogView")
    }

    fun setCallbackForState(callback: StateCallback<E>?) {
        this.stateCallback = callback
    }
}


