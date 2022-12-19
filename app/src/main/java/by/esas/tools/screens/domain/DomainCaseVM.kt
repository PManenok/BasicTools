package by.esas.tools.screens.domain

import android.util.Base64
import by.esas.tools.R
import by.esas.tools.app_domain.usecase.PlayRandomUseCase
import by.esas.tools.base.AppVM
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.domain.util.decryptAES
import by.esas.tools.domain.util.decryptRSA
import by.esas.tools.domain.util.encryptAES
import by.esas.tools.domain.util.generateKey
import by.esas.tools.domain.util.generateRSAKey
import by.esas.tools.logger.handler.ShowErrorType
import javax.inject.Inject

class DomainCaseVM @Inject constructor(
    private val playRandom: PlayRandomUseCase
) : AppVM() {

    fun playRandomNumber(range: Int, number: Int) {
        playRandom.rangeNumbers = range
        playRandom.number = number
        playRandom.execute {
            onComplete { result ->
                val dialog = MessageDialog()
                dialog.setMessage(result)
                dialog.setTitle(R.string.domain_case_congratulations)
                dialog.setPositiveButton(R.string.ok)
                showDialog(dialog)
            }
            onError { errorModel ->
                handleError(errorModel, showType = ShowErrorType.SHOW_ERROR_DIALOG.name)
            }
        }
    }

    fun encryptAES(data: String): Pair<String, String> {
        val secret = generateKey()
        val encryptData = encryptAES(data, secret)
        val decryptData = decryptAES(encryptData, secret)

        return Pair(encryptData, decryptData)
    }

    fun encryptRSA(data: String): Pair<String, String> {
        val pairKey = generateRSAKey()
        val publicKey: String = Base64.encodeToString(pairKey.public.encoded, Base64.NO_WRAP)
        val privateStr: String = Base64.encodeToString(pairKey.private.encoded, Base64.NO_WRAP)
        val encryptData = by.esas.tools.domain.util.encryptRSA(data, publicKey)
        val decryptData = decryptRSA(encryptData, privateStr)

        return Pair(encryptData, decryptData)
    }
}
