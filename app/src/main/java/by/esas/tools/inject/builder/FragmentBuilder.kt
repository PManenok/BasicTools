package by.esas.tools.inject.builder

import by.esas.tools.menu.MenuFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    /* @ContributesAndroidInjector
     abstract fun bindLoadingFragment(): LoadingFragment*/

    @ContributesAndroidInjector
    abstract fun bindMenuFragment(): MenuFragment

}
