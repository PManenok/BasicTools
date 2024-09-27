package by.esas.tools.screens.access_container

import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.accesscontainer.dialog.BiometricUserInfo
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.accesscontainer.support.ITypeManager
import by.esas.tools.base.AppVM
import by.esas.tools.logger.ILogger
import by.esas.tools.screens.access_container.utility.Container
import by.esas.tools.screens.access_container.utility.Executor
import by.esas.tools.screens.access_container.utility.SecretProvider
import by.esas.tools.util.TAGk
import by.esas.tools.utils.logger.ErrorModel
import javax.inject.Inject

class AccessContainerVM @Inject constructor() : AppVM() {

    lateinit var refresher: Container
    lateinit var provider: SecretProvider
    var user: String = "user"
    var preferredType: AuthType = AuthType.PIN_AUTH
    val authTypes = listOf(AuthType.NONE, AuthType.PIN_AUTH, AuthType.BIOMETRIC_AUTH)

    val preferredTypeText = MutableLiveData(getAuthTypeText(preferredType))
    val currentToken = MutableLiveData("")
    val currentRefresh = MutableLiveData("")
    val correctPassword = MutableLiveData("12345678")
    val pinAvailable = MutableLiveData(false)
    val biomAvailable = MutableLiveData(false)
    val refreshExplicitly = MutableLiveData(false)
    val isBiometricAvailable = MutableLiveData(false)
    val noToken = MutableLiveData(false)

    val errorOnRefreshWithToken = MutableLiveData(false)
    val errorOnGetSecrets = MutableLiveData(false)
    val errorOnRefresh = MutableLiveData(false)
    val errorOnRefreshWithSecret = MutableLiveData(false)
    val errorOnCheckSecret = MutableLiveData(false)
    val errorOnCreateSecret = MutableLiveData(false)

    private var userIv: ByteArray = ByteArray(16)
    private val containerLogger = ILogger<ErrorModel>().apply {
        setTag(Container::class.TAGk)
    }

    fun getAuthTypeText(type: AuthType): String {
        val resId = when (type) {
            AuthType.PIN_AUTH -> R.string.label_pin
            AuthType.BIOMETRIC_AUTH -> R.string.label_biom
            AuthType.NONE -> R.string.label_password
        }
        return App.appContext.getString(resId)
    }

    fun initialize() {
        val executor = Executor(ILogger<ErrorModel>().apply {
            setTag(Executor::class.TAGk)
        })
        provider = executor.provider
        refresher = Container.getInstance(
            logger = containerLogger,
            appMapper = mapper,
            executor = executor,
            typeManager = provideTypeManager(),
            userInfo = provideUserInfo()
        )
    }

    fun onPickPreferredType() {
        disableControls()
        val dialog: by.esas.tools.dialog_message.MessageDialog =
            by.esas.tools.dialog_message.MessageDialog()
        dialog.setRequestKey(AccessContainerFragment.PREFERRED_TYPE_PICKER)
        dialog.setTitle(R.string.refresher_preferred_lable)
        dialog.setItems(authTypes.map {
            by.esas.tools.dialog_message.MessageDialog.ItemInfo(
                it.name,
                getAuthTypeText(it)
            )
        })
        dialog.isCancelable = true
        showDialog(dialog)
    }

    fun onRefreshAccess() {
        disableControls()
        updateProvider()
        refresher.refresh({
            currentToken.value = it
            currentRefresh.value = refresher.getRefresh()
            showMessage("Refresh access result: $it")
            enableControls()
        }, {
            handleError(it)
        }, {
            showMessage("Refresh access cancelled")
            enableControls()
        })
    }

    fun onCheckAccess() {
        disableControls()
        updateProvider()
        refresher.checkAccess(refreshExplicitly.value ?: false) {
            onComplete {
                currentToken.value = it
                currentRefresh.value = refresher.getRefresh()
                showMessage("Check access result: $it")
                enableControls()
            }
            onError {
                handleError(it)
            }
            onCancel {
                showMessage("Check access cancelled")
                enableControls()
            }
            onCancellation {
                handleError(it)
            }
        }
    }

    fun onGetSecret() {
        disableControls()
        updateProvider()
        refresher.getSecret {
            onComplete {
                showMessage("Get secret result: $it")
                enableControls()
            }
            onError {
                handleError(it)
            }
            onCancel {
                showMessage("Get secret cancelled")
                enableControls()
            }
            onCancellation {
                handleError(it)
            }
        }
    }

    fun onSaveRefresh() {
        disableControls()
        updateProvider()
        refresher.saveRefresh("newRefresh") {
            onComplete {
                currentToken.value = it
                currentRefresh.value = refresher.getRefresh()
                showMessage("Save refresh token \"newRefresh\" result: $it")
                enableControls()
            }
            onError {
                handleError(it)
            }
            onCancel {
                showMessage("Save refresh token cancelled")
                enableControls()
            }
            onCancellation {
                handleError(it)
            }
        }
    }

    private fun updateProvider() {
        if (noToken.value == true) {
            refresher.setToken(Token(currentToken.value ?: "", ""))
            currentRefresh.value = ""
        } else if (refresher.getRefresh().isBlank()) {
            val token = provider.getCurrentToken()
            refresher.setToken(token)
            currentToken.value = token.accessToken
            currentRefresh.value = token.refreshToken
        }

        val list = mutableListOf<AuthType>()
        if (pinAvailable.value == true) {
            list.add(AuthType.PIN_AUTH)
        }
        if (biomAvailable.value == true) {
            list.add(AuthType.BIOMETRIC_AUTH)
        }
        provider.authTypes = list
        provider.correctPassword = correctPassword.value.orEmpty()
        provider.errorOnRefreshWithToken = errorOnRefreshWithToken.value ?: false
        provider.errorOnGetSecrets = errorOnGetSecrets.value ?: false
        provider.errorOnRefresh = errorOnRefresh.value ?: false
        provider.errorOnRefreshWithSecret = errorOnRefreshWithSecret.value ?: false
        provider.errorOnCheckSecret = errorOnCheckSecret.value ?: false
        provider.errorOnCreateSecret = errorOnCreateSecret.value ?: false
    }

    private fun provideTypeManager(): ITypeManager {
        return object : ITypeManager {
            override fun getPreferredType(): AuthType {
                // this way we can check refresher's behavior when there is not appropriate preferred type
                return preferredType
            }

            override fun putPreferredType(type: AuthType) {
                preferredTypeText.value = getAuthTypeText(type)
                preferredType = type
            }
        }
    }

    private fun provideUserInfo(): BiometricUserInfo {
        return object : BiometricUserInfo {
            override fun getCurrentUser(): String {
                return user
            }

            override fun setCurrentUserIv(iv: ByteArray) {
                userIv = iv
            }

            override fun getCurrentUserIv(): ByteArray {
                return userIv
            }
        }
    }

    private fun showMessage(msg: String) {
        val dialog: by.esas.tools.dialog_message.MessageDialog =
            by.esas.tools.dialog_message.MessageDialog()
        dialog.setMessage(msg)
        dialog.isCancelable = true
        showDialog(dialog)
    }
}