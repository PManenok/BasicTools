package by.esas.tools.inject.builder

import by.esas.tools.screens.baseui.navigation.BaseUINavigationFragment
import by.esas.tools.screens.baseui.navigation.BaseUINavigationSecondFragment
import by.esas.tools.screens.baseui.other.BaseUIFunctionalityFragment
import by.esas.tools.screens.baseui.ui.BaseUIThemeFragment
import by.esas.tools.screens.listheader.ListheaderFragment
import by.esas.tools.screens.custom_switch.CustomSwitchFragment
import by.esas.tools.screens.cardline.CardlineFragment
import by.esas.tools.screens.cardline.dynamic.DynamicCardlineFragment
import by.esas.tools.screens.custom_switch.program.CustomSwitchProgramFragment
import by.esas.tools.screens.listheader.dynamic.DynamicListheaderFragment
import by.esas.tools.screens.dialog.DynamicMessageDialogFragment
import by.esas.tools.screens.dialog.bottom_dialog.BottomDialogFragment
import by.esas.tools.screens.domain.DomainCaseFragment
import by.esas.tools.screens.inputfield_view.end_icon.InputfieldViewEndIconFragment
import by.esas.tools.screens.inputfield_view.input_field.InputfieldViewFragment
import by.esas.tools.screens.inputfield_view.start_icon.InputfieldViewStartIconFragment
import by.esas.tools.screens.menu.MenuFragment
import by.esas.tools.screens.numpad.NumpadImageFragment
import by.esas.tools.screens.numpad.numpad_text.NumpadTextFragment
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
    abstract fun bindDynamicListheaderFragment(): DynamicListheaderFragment

    @ContributesAndroidInjector
    abstract fun bindCustomSwitchFragment(): CustomSwitchFragment

    @ContributesAndroidInjector
    abstract fun bindCustomSwitchProgramFragment(): CustomSwitchProgramFragment

    @ContributesAndroidInjector
    abstract fun bindTopbarFragment(): TopbarFragment

    @ContributesAndroidInjector
    abstract fun bindCardlineFragment(): CardlineFragment

    @ContributesAndroidInjector
    abstract fun bindDynamicCardlineFragment(): DynamicCardlineFragment

    @ContributesAndroidInjector
    abstract fun bindDynamicMessageDialogFragment(): DynamicMessageDialogFragment

    @ContributesAndroidInjector
    abstract fun bindBottomDialogFragment(): BottomDialogFragment

    @ContributesAndroidInjector
    abstract fun bindInputfieldViewFragment(): InputfieldViewFragment

    @ContributesAndroidInjector
    abstract fun bindInputfieldViewEndIconFragment(): InputfieldViewEndIconFragment

    @ContributesAndroidInjector
    abstract fun bindInputfieldViewStartIconFragment(): InputfieldViewStartIconFragment

    @ContributesAndroidInjector
    abstract fun bindNumpadTextFragment(): NumpadTextFragment

    @ContributesAndroidInjector
    abstract fun bindBaseUINavigationFragment(): BaseUINavigationFragment

    @ContributesAndroidInjector
    abstract fun bindBaseUINavigationSecondFragment(): BaseUINavigationSecondFragment

    @ContributesAndroidInjector
    abstract fun bindBaseUIFunctionalityFragment(): BaseUIFunctionalityFragment

    @ContributesAndroidInjector
    abstract fun bindBaseUIThemeFragment(): BaseUIThemeFragment

    @ContributesAndroidInjector
    abstract fun bindDomainCaseFragment(): DomainCaseFragment
}
