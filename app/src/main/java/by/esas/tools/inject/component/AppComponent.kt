package by.esas.tools.inject.component

import android.app.Application
import by.esas.tools.App
import by.esas.tools.inject.builder.ActivityBuilder
import by.esas.tools.inject.module.ContextModule
import by.esas.tools.inject.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ContextModule::class,
        ActivityBuilder::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Suppress("UNCHECKED_CAST")
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}