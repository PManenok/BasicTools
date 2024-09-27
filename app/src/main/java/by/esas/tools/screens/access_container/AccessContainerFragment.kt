package by.esas.tools.screens.access_container

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.base.AppFragment
import by.esas.tools.biometric_decryption.checkBiometricSupport
import by.esas.tools.databinding.FMainRefresherBinding
import by.esas.tools.dialog_core.Config
import by.esas.tools.dialog_message.MessageDialog
import by.esas.tools.getErrorMessage
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorMessageHelper
import by.esas.tools.utils.logger.ErrorModel

class AccessContainerFragment : AppFragment<AccessContainerVM, FMainRefresherBinding>() {

    companion object {

        const val PREFERRED_TYPE_PICKER: String = "PREFERRED_TYPE_PICKER"
        val DEFAULT_TOKEN: Token = Token("accessToken", "refreshToken")
    }

    override val fragmentDestinationId = R.id.accessContainerFragment

    override fun provideLayoutId(): Int = R.layout.f_main_refresher

    override fun provideViewModel(): AccessContainerVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(AccessContainerVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize()
        viewModel.isBiometricAvailable.value =
            activity?.applicationContext?.let {
                checkBiometricSupport(it) { tag: String, msg: String ->
                    logger.logCategory(ILogger.CATEGORY_ERROR, tag, msg)
                }
            } ?: false

        viewModel.refresher.setActivity(requireActivity())
        viewModel.refresher.setUserId(viewModel.user)
        viewModel.currentToken.value = DEFAULT_TOKEN.accessToken
        viewModel.currentRefresh.value = DEFAULT_TOKEN.refreshToken
        viewModel.provider.setToken(DEFAULT_TOKEN)
        viewModel.refresher.setToken(DEFAULT_TOKEN)
    }

    override fun provideRequestKeys(): List<String> {
        return super.provideRequestKeys().plus(listOf(PREFERRED_TYPE_PICKER))
    }

    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return if (requestKey == PREFERRED_TYPE_PICKER) {
            FragmentResultListener { key, result ->
                val actionName = result.getString(Config.DIALOG_USER_ACTION)
                if (actionName == MessageDialog.USER_ACTION_ITEM_PICKED) {
                    val name = result.getString(MessageDialog.ITEM_NAME)
                    val code = result.getString(MessageDialog.ITEM_CODE) ?: AuthType.NONE.name
                    viewModel.preferredTypeText.value = name
                    viewModel.preferredType = AuthType.valueOf(code)
                    viewModel.enableControls()
                } else {
                    viewModel.enableControls()
                }
            }
        } else {
            super.provideFragmentResultListener(requestKey)
        }
    }

    override fun provideErrorStringHelper(): ErrorMessageHelper<ErrorModel> {
        logger.order(TAG, "provideErrorHandler")
        return object : ErrorMessageHelper<ErrorModel> {

            override fun getErrorMessage(error: ErrorModel): String {
                return getErrorMessage(error.getStatusAsEnum(), null)
            }
        }
    }
}