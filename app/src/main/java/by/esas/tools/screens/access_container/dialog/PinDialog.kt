package by.esas.tools.screens.access_container.dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import by.esas.tools.App
import by.esas.tools.BR
import by.esas.tools.R
import by.esas.tools.checker.Checking
import by.esas.tools.databinding.DfPinBinding
import by.esas.tools.dialog.BindingDialogFragment
import by.esas.tools.dialog.Config
import by.esas.tools.numpad.INumPadHandler
import com.google.android.material.textfield.TextInputEditText

class PinDialog : BindingDialogFragment<DfPinBinding>() {

    override val TAG: String = PinDialog::class.java.simpleName

    companion object {

        const val USER_ACTION_PIN_COMPLETED: String = "USER_ACTION_PIN_COMPLETED"
        const val RESULT_PIN: String = "RESULT_PIN"
        const val USER_ACTION_ANOTHER_CLICKED: String = "USER_ACTION_ANOTHER_CLICKED"
    }

    override fun provideLayoutId(): Int {
        return R.layout.df_pin
    }

    override fun provideVariableId(): Int {
        return BR.handler
    }

    override fun provideSwitchableList(): List<TextInputEditText> {
        return emptyList()
    }

    override fun provideValidationList(): List<Checking> {
        return emptyList()
    }

    private val builder: StringBuilder = StringBuilder()

    private var compare: String = ""
    private var titleRes: Int = R.string.dialog_pin_title
    private var confirmBtnTextRes: Int = R.string.dialog_pin_create_btn
    val btnEnabled = ObservableBoolean(true)
    val confirmBtnEnable = ObservableBoolean(false)
    val showPinError = ObservableBoolean(false)
    val showAnotherBtn = ObservableBoolean(false)
    val showConfirmBtn = ObservableBoolean(false)
    val confirmBtnText = ObservableField<String>("")

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("btnEnabled", btnEnabled.get())
        outState.putBoolean("showAnotherBtn", showAnotherBtn.get())
        outState.putBoolean("showConfirmBtn", showConfirmBtn.get())
        outState.putBoolean("showPinError", showPinError.get())
        outState.putString("old", compare)
        outState.putString("builder", builder.toString())
        outState.putInt("titleRes", titleRes)
        outState.putInt("confirmBtnTextRes", confirmBtnTextRes)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            btnEnabled.set(savedInstanceState.getBoolean("btnEnabled", true))
            showAnotherBtn.set(savedInstanceState.getBoolean("showAnotherBtn", false))
            showConfirmBtn.set(savedInstanceState.getBoolean("showConfirmBtn", false))
            showPinError.set(savedInstanceState.getBoolean("showPinError", false))
            compare = savedInstanceState.getString("old", "") ?: ""
            builder.clear()
            builder.append(savedInstanceState.getString("builder", "") ?: "")
            titleRes = (savedInstanceState.getInt("titleRes", R.string.dialog_pin_title))
            confirmBtnTextRes = (savedInstanceState.getInt("confirmBtnTextRes", R.string.dialog_pin_create_btn))
            updateScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableControls()
        builder.clear()

        binding.dfPinNumPad.setNumpadHandler(object : INumPadHandler {
            override fun onNumClick(num: Int) {
                showPinError.set(false)
                if (btnEnabled.get()) {
                    disableControls()
                    if (builder.length < 5) {
                        addPin(num)
                    } else if (builder.length < 6) {
                        addLastPin(num)
                    } else {
                        enableControls()
                    }
                }
            }

            private fun addPin(num: Int) {
                binding.dfPinView.fillPin()
                builder.append(num)
                enableControls()
            }

            private fun addLastPin(num: Int) {
                binding.dfPinView.fillPin()
                builder.append(num)
                if (!showConfirmBtn.get()) {
                    binding.dfPinView.postOnAnimationDelayed({
                        logger.logInfo(builder.length.toString())
                        resultBundle.clear()
                        resultBundle.putString(Config.DIALOG_USER_ACTION, USER_ACTION_PIN_COMPLETED)
                        resultBundle.putString(RESULT_PIN, builder.toString())
                        builder.clear()
                        binding.dfPinView.clearPins()
                        dismiss()
                        enableControls()
                    }, 300L)
                } else {
                    if (compare.isNotBlank() && compare != builder.toString()) {
                        showPinError.set(true)
                    }
                    enableControls()
                }
            }

            override fun onRightIconClick() {
                disableControls()
                showPinError.set(false)
                if (builder.isNotEmpty()) {
                    val length = if (builder.length <= 1) 0 else builder.length - 1
                    builder.setLength(length)
                    binding.dfPinView.unFillPin()
                }
                enableControls()
            }
        })
        /*restoreBtn?.setOnLongClickListener {
            return@setOnLongClickListener if (btnEnabled.get()) {
                disableControls()
                showPinError.set(false)
                builder.clear()
                binding.dfPinView.clearPins()
                enableControls()
                true
            } else
                false
        }*/
        updateScreen()
    }

    private fun updateScreen() {
        binding.dfPinTopBarTitle.setText(titleRes)
        binding.dfPinView.setPin(builder.length)
        confirmBtnText.set(App.appContext.getString(confirmBtnTextRes))
    }

    fun onBackClick() {
        disableControls()
        //full = false
        builder.clear()
        binding.dfPinView.clearPins()
        resultBundle.clear()
        resultBundle.putString(Config.DIALOG_USER_ACTION, Config.CANCEL_DIALOG)
        dismiss()
    }

    fun onAnotherClick() {
        disableControls()
        //full = false
        builder.clear()
        binding.dfPinView.clearPins()
        resultBundle.clear()
        resultBundle.putString(Config.DIALOG_USER_ACTION, USER_ACTION_ANOTHER_CLICKED)
        dismiss()
        enableControls()
    }

    fun onConfirmClick() {
        disableControls()
        if (compare.isNotBlank() && compare != builder.toString()) {
            Toast.makeText(requireContext(), R.string.error_app_not_equal, Toast.LENGTH_SHORT).show()
            enableControls()
        } else {
            resultBundle.clear()
            resultBundle.putString(Config.DIALOG_USER_ACTION, USER_ACTION_PIN_COMPLETED)
            resultBundle.putString(RESULT_PIN, builder.toString())
            builder.clear()
            binding.dfPinView.clearPins()
            dismiss()
            enableControls()
        }
    }

    override fun disableControls() {
        btnEnabled.set(false)
        confirmBtnEnable.set(false)
        binding.dfPinNumPad.disableNumpadView()
        super.disableControls()
    }

    override fun enableControls() {
        btnEnabled.set(true)
        confirmBtnEnable.set(builder.length == 6)
        binding.dfPinNumPad.enableNumpadView()
        super.enableControls()
    }

    fun setTitle(resId: Int) {
        titleRes = if (resId == -1) R.string.dialog_pin_title else resId
    }

    fun setConfirmBtn(showBtn: Boolean, textResId: Int = -1, compare: String = "") {
        this.compare = compare
        confirmBtnTextRes = if (textResId == -1) R.string.common_continue_button else textResId
        showConfirmBtn.set(showBtn)
        if (showBtn) showAnotherBtn.set(false)
    }

    fun setHasAnother(value: Boolean) {
        showAnotherBtn.set(value)
        if (value) showConfirmBtn.set(false)
    }
}