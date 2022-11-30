package by.esas.tools.inject.builder

import androidx.lifecycle.ViewModel
import by.esas.tools.basedaggerui.factory.AssistedSavedStateViewModelFactory
import by.esas.tools.basedaggerui.qualifier.ViewModelKey
import by.esas.tools.screens.MainVM
import by.esas.tools.screens.cardline.CardlineVM
import by.esas.tools.screens.custom_switch.CustomSwitchVM
import by.esas.tools.screens.listheader.ListheaderVM
import by.esas.tools.screens.menu.MenuVM
import by.esas.tools.screens.numpad.NumpadImageVM
import by.esas.tools.screens.pin_view.PinViewVM
import by.esas.tools.screens.recycler.base.RecyclerVM
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
    @ViewModelKey(CustomSwitchVM::class)
    abstract fun bindCustomSwitchVM(customSwitchVM: CustomSwitchVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopbarVM::class)
    abstract fun bindTopbarVM(topbarVM: TopbarVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CardlineVM::class)
    abstract fun bindCardlineVM(viewModel: CardlineVM): ViewModel

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
}
