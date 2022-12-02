package by.esas.tools.inject.builder

import by.esas.tools.screens.cardline.CardlineFragment
import by.esas.tools.screens.custom_switch.CustomSwitchFragment
import by.esas.tools.screens.listheader.ListheaderFragment
import by.esas.tools.screens.menu.MenuFragment
import by.esas.tools.screens.numpad.NumpadImageFragment
import by.esas.tools.screens.pin_view.PinViewFragment
import by.esas.tools.screens.recycler.base.RecyclerFragment
import by.esas.tools.screens.recycler.custom.CustomRecyclerFragment
import by.esas.tools.screens.recycler.simple.SimpleRecyclerFragment
import by.esas.tools.screens.recycler.sticky_case.StickyCaseFragment
import by.esas.tools.screens.saved_state_vm.SavedStateFragment
import by.esas.tools.screens.topbar.TopbarFragment
import by.esas.tools.screens.util.UtilKeyboardFragment
import by.esas.tools.screens.util.switchManager.UtilSwitchManagerFragment
import by.esas.tools.screens.util.utils.UtilsFragment
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

    @ContributesAndroidInjector
    abstract fun bindNumpadImageFragment(): NumpadImageFragment

    @ContributesAndroidInjector
    abstract fun bindUtilKeyboardFragment(): UtilKeyboardFragment

    @ContributesAndroidInjector
    abstract fun bindUtilsFragment(): UtilsFragment

    @ContributesAndroidInjector
    abstract fun bindUtilSwitchManagerFragment(): UtilSwitchManagerFragment


    @ContributesAndroidInjector
    abstract fun bindListheaderFragment(): ListheaderFragment

    @ContributesAndroidInjector
    abstract fun bindCustomSwitchFragment(): CustomSwitchFragment

    @ContributesAndroidInjector
    abstract fun bindTopbarFragment(): TopbarFragment

    @ContributesAndroidInjector
    abstract fun bindCardlineFragment(): CardlineFragment

    @ContributesAndroidInjector
    abstract fun bindRecyclerFragment(): RecyclerFragment

    @ContributesAndroidInjector
    abstract fun bindStickyCaseFragment(): StickyCaseFragment

    @ContributesAndroidInjector
    abstract fun bindSimpleRecyclerFragment(): SimpleRecyclerFragment

    @ContributesAndroidInjector
    abstract fun bindCustomRecyclerFragment(): CustomRecyclerFragment
}
