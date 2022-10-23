package by.esas.tools.screens.dialog.bottom_dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainBottomDialogBinding
import by.esas.tools.dialog.Config
import by.esas.tools.dialog.Config.DIALOG_USER_ACTION
import by.esas.tools.dialog.CustomBottomDialog
import by.esas.tools.logger.Action

private const val BOTTOM_DIALOG = "BOTTOM_DIALOG"

class BottomDialogFragment: AppFragment<BottomDialogVM, FMainBottomDialogBinding>() {
    override val fragmentDestinationId = R.id.bottomDialogFragment

    override fun provideLayoutId() = R.layout.f_main_bottom_dialog

    override fun provideViewModel(): BottomDialogVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(BottomDialogVM::class.java)
    }

    override fun provideRequestKeys(): List<String> = listOf(BOTTOM_DIALOG)

    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return if (requestKey == BOTTOM_DIALOG) {
            FragmentResultListener { _, result ->
                val actionName = result.getString(DIALOG_USER_ACTION)
                if (!actionName.isNullOrBlank()) {
                    handleAction(Action(actionName, result))
                } else {
                    enableControls(result)
                }
            }
        } else {
            super.provideFragmentResultListener(requestKey)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDialog(createBottomDialog(), BOTTOM_DIALOG)
    }

    override fun handleAction(action: Action): Boolean {
        when (action.name) {
            Config.DISMISS_DIALOG -> Toast.makeText(
                requireContext(),
                action.parameters?.getString(CustomBottomDialog.BOTTOM_DIALOG_CHECK_RESULT),
                Toast.LENGTH_LONG
            ).show()
            else -> return super.handleAction(action)
        }
        return true
    }

    private fun createBottomDialog(): CustomBottomDialog {
        return CustomBottomDialog().apply {
            setRequestKey(BOTTOM_DIALOG)
            isCancelable = false
        }
    }
}
