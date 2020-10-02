package by.esas.tools.inject.module

import android.app.Application
import android.content.Context
import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.inject.builder.ViewModelFactoryBuilder
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Module(
    includes = [
        ViewModelFactoryBuilder::class
    ]
)
class ContextModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideMainDispatcherContext(): CoroutineContext = Dispatchers.Main

    @Provides
    @Singleton
    fun provideCrashlyticsMapper(moshi: Moshi, logger: ILogger<AppErrorStatusEnum>): AppErrorMapper {
        return AppErrorMapper(moshi, logger)
    }

    @Provides
    fun provideLogger(): ILogger<AppErrorStatusEnum> {
        return LoggerImpl().apply { setTag("LoggerImpl") }
    }
}
