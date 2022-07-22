package by.esas.tools.screens.menu

import androidx.databinding.ObservableBoolean
import androidx.navigation.NavDirections
import by.esas.tools.simple.AppVM
import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.screens.menu.recycler.CaseAdapter
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.entity.ModuleEnum
import by.esas.tools.logger.Action
import by.esas.tools.usecase.SearchCaseUseCase
import javax.inject.Inject

class MenuVM @Inject constructor(
    val mapper: AppErrorMapper,
    val searchCase: SearchCaseUseCase
    ) : AppVM() {
    override fun provideMapper(): IErrorMapper<ErrorModel> {
        return mapper
    }

    var prevSearch = ""
    var search = ""
    var isEmpty = ObservableBoolean(false)

    val allCases: MutableList<CaseItemInfo> = mutableListOf()

    val caseAdapter = CaseAdapter(
        onClick = {
            logger.logInfo("item click")
        }
    )

    init {
        addCaseItem( "Numpad view case", listOf(ModuleEnum.CARDLINE, ModuleEnum.LISTHEADER))
        addCaseItem("Case for PinView", listOf(ModuleEnum.DIALOG, ModuleEnum.BASE_DAGGER_UI))
        addCaseItem( "Case for BaseUi", listOf(ModuleEnum.INPUTFIELD_VIEW, ModuleEnum.DIALOG))
        addCaseItem( "Simple case", listOf(ModuleEnum.INPUTFIELD_VIEW, ModuleEnum.BASE_DAGGER_UI))
        addCaseItem( "Test for dialog module", listOf(ModuleEnum.DIALOG, ModuleEnum.CHECKER))
        addCaseItem( "Test dialog with errors", listOf(ModuleEnum.BASE_UI, ModuleEnum.LISTHEADER))
        addCaseItem( "Change theme case", listOf(ModuleEnum.LOGGER, ModuleEnum.BASE_DAGGER_UI))
        addCaseItem( "Change Language case", listOf(ModuleEnum.CUSTOMSWITCH, ModuleEnum.BASE_DAGGER_UI))
    }

    fun updateAdapter(list: List<CaseItemInfo>) {
        caseAdapter.addItems(list)
        isEmpty.set(list.isEmpty())
    }

    fun onSearchChanged(value: String) {
        if (prevSearch != value) {
            disableControls()
            prevSearch = value
            searchCase.caseItems = allCases
            searchCase.search = search
            searchCase.execute{
                onComplete { itemsList ->
                    updateAdapter(itemsList)
                    enableControls()
                }
                onError {
                    handleError(error = it)
                }
            }
        }
    }

    private fun addCaseItem(name: String, modulesList: List<ModuleEnum>, direction: NavDirections? = null){
        allCases.add(CaseItemInfo(allCases.size, name, modulesList, direction))
    }
}