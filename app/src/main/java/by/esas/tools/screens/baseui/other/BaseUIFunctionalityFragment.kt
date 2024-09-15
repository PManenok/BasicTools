package by.esas.tools.screens.baseui.other

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainBaseuiFunctionalityBinding
import by.esas.tools.logger.Action
import by.esas.tools.logger.handler.ErrorAction
import by.esas.tools.logger.handler.ShowErrorType
import by.esas.tools.util_ui.SwitchManager
import by.esas.tools.utils.logger.ErrorModel

class BaseUIFunctionalityFragment : AppFragment<BaseUIFunctionalityVM, FMainBaseuiFunctionalityBinding>() {

    override val fragmentDestinationId = R.id.baseuiFunctionalityFragment

    override fun provideLayoutId() = R.layout.f_main_baseui_functionality

    override fun provideViewModel(): BaseUIFunctionalityVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(BaseUIFunctionalityVM::class.java)
    }

    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fBaseuiFunctionalityMessageDialogFragmentBtn,
            binding.fBaseuiFunctionalityMessageDialogVmBtn,
            binding.fBaseuiFunctionalityInputField,
            binding.fBaseuiFunctionalityPermissionCheckBtn,
            binding.fBaseuiFunctionalityPermissionRequestBtn,
            binding.fBaseuiFunctionalityPermissionsGroupBtn
        )
    }

    override var switcher: by.esas.tools.util_ui.SwitchManager = object : by.esas.tools.util_ui.SwitchManager() {
        override fun enableView(view: View): Boolean {
            return if (view is Button) {
                view.isEnabled = true
                true
            } else
                super.enableView(view)
        }

        override fun disableView(view: View): Boolean {
            return if (view is Button) {
                view.isEnabled = false
                true
            } else
                super.disableView(view)
        }
    }

    override fun providePermissions(parameters: Bundle?): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        else
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun providePermissionResultCallback(): ActivityResultCallback<Map<String, Boolean>> {
        return ActivityResultCallback<Map<String, Boolean>> { result ->
            if (result.all { it.value }) {
                Toast.makeText(
                    context,
                    resources.getString(R.string.baseui_functionality_permissions_confirmed),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.baseui_functionality_permissions_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fBaseuiFunctionalityMessageDialogFragmentBtn.setOnClickListener {
            val dialog = by.esas.tools.dialog_message.MessageDialog()
            dialog.setMessage(resources.getString(R.string.baseui_functionality_test_message))
            dialog.setTitle(resources.getString(R.string.baseui_functionality_test_dialog))
            showDialog(dialog, "DIALOG")
        }

        binding.fBaseuiFunctionalityMessageDialogVmBtn.setOnClickListener {
            viewModel.showMessageDialog()
        }

        binding.fBaseuiSwitcher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.enableControls()
            else viewModel.disableControls()
        }

        binding.fBaseuiFunctionalityPermissionCheckBtn.setOnClickListener {
            if (checkPermissions(providePermissions(null), null, false))
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.baseui_functionality_permissions_confirmed),
                    Toast.LENGTH_LONG
                ).show()
            else
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.baseui_functionality_permissions_denied),
                    Toast.LENGTH_LONG
                ).show()
        }

        binding.fBaseuiFunctionalityPermissionRequestBtn.setOnClickListener {
            if (checkPermissions(providePermissions(null), null, false))
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.baseui_functionality_permissions_already_confirmed),
                    Toast.LENGTH_LONG
                ).show()
            else
                handleAction(Action(Action.ACTION_CHECK_AND_REQUEST_PERMISSIONS))
        }

        binding.fBaseuiFunctionalityPermissionsGroupBtn.setOnClickListener {
            val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE)
            if (checkPermissions(permissions, null, false))
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.baseui_functionality_permissions_already_confirmed),
                    Toast.LENGTH_LONG
                ).show()
            else
                checkPermissions(permissions, null, true)
        }

        binding.fBaseuiFunctionalityVmErrorDialogBtn.setOnClickListener {
            viewModel.handleError(
                ErrorModel(400, AppErrorStatusEnum.UNKNOWN_ERROR),
                showType = ShowErrorType.SHOW_ERROR_DIALOG.name
            )
        }
        binding.fBaseuiFunctionalityVmErrorMessageBtn.setOnClickListener {
            viewModel.handleError(
                ErrorModel(400, AppErrorStatusEnum.UNKNOWN_ERROR),
                showType = ShowErrorType.SHOW_ERROR_MESSAGE.name
            )
        }
        binding.fBaseuiFunctionalityVmErrorNothingBtn.setOnClickListener {
            viewModel.handleError(
                ErrorModel(400, AppErrorStatusEnum.UNKNOWN_ERROR),
                showType = ShowErrorType.SHOW_NOTHING.name
            )
        }

        binding.fBaseuiFunctionalityErrorDialogBtn.setOnClickListener {
            handleError(
                ErrorAction.Companion.create(
                    ErrorModel(400, AppErrorStatusEnum.UNKNOWN_ERROR),
                    showType = ShowErrorType.SHOW_ERROR_DIALOG.name,
                    ErrorAction.ACTION_ERROR,
                    null
                )
            )
        }
        binding.fBaseuiFunctionalityErrorMessageBtn.setOnClickListener {
            handleError(
                ErrorAction.Companion.create(
                    ErrorModel(400, AppErrorStatusEnum.UNKNOWN_ERROR),
                    showType = ShowErrorType.SHOW_ERROR_MESSAGE.name,
                    ErrorAction.ACTION_ERROR,
                    null
                )
            )
        }
        binding.fBaseuiFunctionalityErrorNothingBtn.setOnClickListener {
            handleError(
                ErrorAction.Companion.create(
                    ErrorModel(400, AppErrorStatusEnum.UNKNOWN_ERROR),
                    showType = ShowErrorType.SHOW_NOTHING.name,
                    ErrorAction.ACTION_ERROR,
                    null
                )
            )
        }
    }

    override fun switchControlsOn() {
        super.switchControlsOn()

        binding.fBaseuiSwitcher.isChecked = true
    }
}
