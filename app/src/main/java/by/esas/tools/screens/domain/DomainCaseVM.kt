package by.esas.tools.screens.domain

import by.esas.tools.app_domain.usecase.PlayRandomUseCase
import by.esas.tools.base.AppVM
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.logger.handler.ShowErrorType
import javax.inject.Inject

class DomainCaseVM @Inject constructor(
    private val playRandom: PlayRandomUseCase
): AppVM() {

    fun playRandomNumber(range: Int, number: Int) {
        playRandom.rangeNumbers = range
        playRandom.number = number
        playRandom.execute {
            onComplete { result ->
                val dialog = MessageDialog()
                dialog.setMessage(result)
                showDialog(dialog)
            }
            onError { errorModel ->
                handleError(errorModel, showType = ShowErrorType.SHOW_ERROR_DIALOG.name)
            }
        }
    }
}