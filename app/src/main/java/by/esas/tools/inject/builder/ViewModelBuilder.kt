package by.esas.tools.inject.builder

import androidx.lifecycle.ViewModel
import by.esas.tools.AddInvoiceViewModel
import by.esas.tools.screens.MainVM
import by.esas.tools.basedaggerui.factory.AssistedSavedStateViewModelFactory
import by.esas.tools.basedaggerui.qualifier.ViewModelKey
import by.esas.tools.screens.menu.MenuVM
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
    @ViewModelKey(AddInvoiceViewModel::class)
    abstract fun bindVMFactory(f: AddInvoiceViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(MenuVM::class)
    abstract fun bindMenuViewModel(menuViewModel: MenuVM): ViewModel
}
