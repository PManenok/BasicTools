package by.esas.tools.inject.builder

import by.esas.tools.screens.menu.MenuFragment
import by.esas.tools.screens.pin_view.PinViewFragment
import by.esas.tools.screens.saved_state_vm.SavedStateFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun bindMenuFragment(): MenuFragment

    @ContributesAndroidInjector
    abstract fun bindPinViewFragment(): PinViewFragment

    @ContributesAndroidInjector
    abstract fun bindSavedStateFragment(): SavedStateFragment
}
