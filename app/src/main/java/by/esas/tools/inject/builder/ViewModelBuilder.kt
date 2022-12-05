package by.esas.tools.inject.builder

import androidx.lifecycle.ViewModel
import by.esas.tools.basedaggerui.factory.AssistedSavedStateViewModelFactory
import by.esas.tools.basedaggerui.qualifier.ViewModelKey
import by.esas.tools.screens.MainVM
import by.esas.tools.screens.baseui.navigation.BaseUINavigationSecondVM
import by.esas.tools.screens.baseui.navigation.BaseUINavigationVM
import by.esas.tools.screens.baseui.other.BaseUIFunctionalityVM
import by.esas.tools.screens.baseui.ui.BaseUIThemeVM
import by.esas.tools.screens.cardline.CardlineVM
import by.esas.tools.screens.cardline.dynamic.DynamicCardlineVM
import by.esas.tools.screens.custom_switch.CustomSwitchVM
import by.esas.tools.screens.custom_switch.program.CustomSwitchProgramVM
import by.esas.tools.screens.dialog.DynamicMessageDialogVM
import by.esas.tools.screens.dialog.bottom_dialog.BottomDialogVM
import by.esas.tools.screens.domain.DomainCaseVM
import by.esas.tools.screens.inputfield_view.end_icon.InputfieldViewEndIconVM
import by.esas.tools.screens.inputfield_view.input_field.InputfieldViewVM
import by.esas.tools.screens.inputfield_view.start_icon.InputfieldViewStartIconVM
import by.esas.tools.screens.listheader.ListheaderVM
import by.esas.tools.screens.listheader.dynamic.DynamicListheaderVM
import by.esas.tools.screens.menu.MenuVM
import by.esas.tools.screens.numpad.NumpadImageVM
import by.esas.tools.screens.numpad.numpad_text.NumpadTextVM
import by.esas.tools.screens.pin_view.PinViewVM
import by.esas.tools.screens.pin_view.dynamic.DynamicPinViewVM
import by.esas.tools.screens.recycler.base.RecyclerVM
import by.esas.tools.screens.recycler.custom.CustomRecyclerVM
import by.esas.tools.screens.recycler.simple.SimpleRecyclerVM
import by.esas.tools.screens.recycler.sticky_case.StickyCaseVM
import by.esas.tools.screens.saved_state_vm.SavedStateVM
import by.esas.tools.screens.topbar.TopbarVM
import by.esas.tools.screens.util.UtilKeyboardVM
import by.esas.tools.screens.util.switchManager.UtilSwitchManagerVM
import by.esas.tools.screens.util.utils.UtilsVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(MainVM::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MenuVM::class)
    abstract fun bindMenuViewModel(menuViewModel: MenuVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PinViewVM::class)
    abstract fun bindPinViewVM(pinViewVM: PinViewVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DynamicPinViewVM::class)
    abstract fun bindDynamicPinViewVM(dynamicPinViewVM: DynamicPinViewVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NumpadImageVM::class)
    abstract fun bindNumpadImageVM(numpadImageVM: NumpadImageVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UtilKeyboardVM::class)
    abstract fun bindUtilKeyboardVM(utilKeyboardVM: UtilKeyboardVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UtilsVM::class)
    abstract fun bindUtilsVM(utilsVM: UtilsVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UtilSwitchManagerVM::class)
    abstract fun bindUtilSwitchManagerVM(utilSwitchManagerVM: UtilSwitchManagerVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListheaderVM::class)
    abstract fun bindListheaderVM(listheaderVM: ListheaderVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DynamicListheaderVM::class)
    abstract fun bindDynamicListheaderVM(dynamicListheaderVM: DynamicListheaderVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CustomSwitchVM::class)
    abstract fun bindCustomSwitchVM(customSwitchVM: CustomSwitchVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CustomSwitchProgramVM::class)
    abstract fun bindCustomSwitchProgramVM(customSwitchProgramVM: CustomSwitchProgramVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopbarVM::class)
    abstract fun bindTopbarVM(topbarVM: TopbarVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CardlineVM::class)
    abstract fun bindCardlineVM(cardlineVM: CardlineVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DynamicCardlineVM::class)
    abstract fun bindDynamicCardlineVM(dynamicCardlineVM: DynamicCardlineVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DynamicMessageDialogVM::class)
    abstract fun bindDynamicMessageDialogVM(dynamicMessageDialogVM: DynamicMessageDialogVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BottomDialogVM::class)
    abstract fun bindBottomDialogVM(bottomDialogVM: BottomDialogVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InputfieldViewEndIconVM::class)
    abstract fun bindInputfieldViewEndIconVM(inputfieldViewEndIconVM: InputfieldViewEndIconVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InputfieldViewVM::class)
    abstract fun bindInputfieldViewVM(inputfieldViewVM: InputfieldViewVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InputfieldViewStartIconVM::class)
    abstract fun bindInputfieldViewStartIconVM(inputfieldViewStartIconVM: InputfieldViewStartIconVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NumpadTextVM::class)
    abstract fun bindNumpadTextVM(numpadTextVM: NumpadTextVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BaseUINavigationSecondVM::class)
    abstract fun bindBaseUINavigationSecondVM(baseUINavigationSecondVM: BaseUINavigationSecondVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BaseUINavigationVM::class)
    abstract fun bindBaseUINavigationVM(baseUINavigationVM: BaseUINavigationVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BaseUIFunctionalityVM::class)
    abstract fun bindBaseUIFunctionalityVM(baseUIFunctionalityVM: BaseUIFunctionalityVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BaseUIThemeVM::class)
    abstract fun bindBaseUIThemeVM(baseUIThemeVM: BaseUIThemeVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DomainCaseVM::class)
    abstract fun bindDomainCaseVM(domainCaseVM: DomainCaseVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedStateVM::class)
    abstract fun bindVMFactory(f: SavedStateVM.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(RecyclerVM::class)
    abstract fun bindRecyclerVM(viewModel: RecyclerVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StickyCaseVM::class)
    abstract fun bindStickyCaseVM(viewModel: StickyCaseVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SimpleRecyclerVM::class)
    abstract fun bindSimpleRecyclerVM(viewModel: SimpleRecyclerVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CustomRecyclerVM::class)
    abstract fun bindCustomRecyclerVM(viewModel: CustomRecyclerVM): ViewModel
}
