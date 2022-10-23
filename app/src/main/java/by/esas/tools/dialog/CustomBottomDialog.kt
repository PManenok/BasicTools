package by.esas.tools.dialog

import android.os.Bundle
import android.view.View
import android.widget.Button
import by.esas.tools.R
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.checker.checks.LengthCheck
import by.esas.tools.databinding.DfCustomBottomBinding
import by.esas.tools.util.SwitchManager
import by.esas.tools.utils.checking.AppChecker
import by.esas.tools.utils.checking.FieldChecking

class CustomBottomDialog: BindingBottomDialogFragment<DfCustomBottomBinding>() {

    companion object {
        const val BOTTOM_DIALOG_CHECK_RESULT = "BOTTOM_DIALOG_CHECK_RESULT"
    }

    override fun provideLayoutId(): Int {
        return R.layout.df_custom_bottom
    }

    override var switcher: SwitchManager = object : SwitchManager() {
        override fun enableView(view: View): Boolean {
            return if (view is Button){
                view.isEnabled = true
                true
            } else {
                super.enableView(view)
            }
        }

        override fun disableView(view: View): Boolean {
            return if (view is Button){
                view.isEnabled = false
                true
            } else {
                super.disableView(view)
            }
        }
    }

    override fun provideSwitchableList(): List<View?> {
        return listOf(binding.dfCustomBottomSendButton, binding.dfCustomBottomInputField)
    }

    override fun provideValidationList(): List<Checking> {
        return listOf(FieldChecking(binding.dfCustomBottomInputField).addCheck(
            LengthCheck(1, 20,
                resources.getString(R.string.validation_text_in_range, 1, 20)
            )
        ))
    }

    override fun provideVariableId(): Int {
        return BR.handler
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dfCustomBottomSwitcher.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked) {
                true -> enableControls()
                false -> disableControls()
            }
        }

        binding.dfCustomBottomSendButton.setOnClickListener {
            disableControls()
            AppChecker().setListener(object : Checker.CheckListener {
                override fun onFailed() {
                    enableControls()
                }

                override fun onSuccess() {
                    enableControls()
                    resultBundle.putString(Config.DIALOG_USER_ACTION, Config.DISMISS_DIALOG)
                    resultBundle.putString(BOTTOM_DIALOG_CHECK_RESULT, binding.dfCustomBottomInputField.getText())
                    dismiss()
                }
            }).validate(provideValidationList())
        }
    }
}