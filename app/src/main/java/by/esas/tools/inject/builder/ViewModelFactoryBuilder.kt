package by.esas.tools.inject.builder

import androidx.lifecycle.ViewModelProvider
import by.esas.tools.basedaggerui.factory.ViewModelFactory
import dagger.Binds
import dagger.Module


@Module(
    includes = [
        (RepositoryBuilder::class),
        (ViewModelBuilder::class)
    ]
)
abstract class ViewModelFactoryBuilder {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
