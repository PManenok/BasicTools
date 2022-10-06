package by.esas.tools.inject.builder

import by.esas.tools.screens.listheader.ListheaderFragment
import by.esas.tools.screens.custom_switch.CustomSwitchFragment
import by.esas.tools.screens.cardline.CardlineFragment
import by.esas.tools.screens.custom_switch.program.CustomSwitchProgramFragment
import by.esas.tools.screens.menu.MenuFragment
import by.esas.tools.screens.numpad.NumpadImageFragment
import by.esas.tools.screens.pin_view.PinViewFragment
import by.esas.tools.screens.pin_view.dynamic.DynamicPinViewFragment
import by.esas.tools.screens.saved_state_vm.SavedStateFragment
import by.esas.tools.screens.util.UtilKeyboardFragment
import by.esas.tools.screens.util.switchManager.UtilSwitchManagerFragment
import by.esas.tools.screens.util.utils.UtilsFragment
import by.esas.tools.screens.topbar.TopbarFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun bindMenuFragment(): MenuFragment

    @ContributesAndroidInjector
    abstract fun bindPinViewFragment(): PinViewFragment

    @ContributesAndroidInjector
    abstract fun bindDynamicPinViewFragment(): DynamicPinViewFragment

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
    abstract fun bindCustomSwitchProgramFragment(): CustomSwitchProgramFragment

    @ContributesAndroidInjector
    abstract fun bindTopbarFragment(): TopbarFragment

    @ContributesAndroidInjector
    abstract fun bindCardlineFragment(): CardlineFragment
}
