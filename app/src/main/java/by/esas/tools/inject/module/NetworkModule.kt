package by.esas.tools.inject.module

import android.content.Context
import by.esas.tools.app_data.db.AppDataBase
import by.esas.tools.app_data.db.dao.CaseStatusDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /*############ Data Base ############*/

    @Singleton
    @Provides
    fun provideAppDataBase(context: Context): AppDataBase = AppDataBase.getInstance(context)

    @Singleton
    @Provides
    fun provideCaseStatusDao(appDataBase: AppDataBase): CaseStatusDao = appDataBase.getCaseStatusDao()

    /*  @Provides
      @Singleton
      fun providesRetrofit(
          moshiConverterFactory: MoshiConverterFactory,
          okHttpClient: OkHttpClient
      ): Retrofit.Builder {
          return Retrofit.Builder()
              .addConverterFactory(moshiConverterFactory)
              .addCallAdapterFactory(CoroutineCallAdapterFactory())
              .client(okHttpClient)
      }

      @Provides
      @Singleton
      fun providesOkHttpClient(logger: ILogger): OkHttpClient {
          val client = OkHttpClient.Builder()
              //  .cache(cache)
              .connectTimeout(30, TimeUnit.SECONDS)
              .writeTimeout(30, TimeUnit.SECONDS)
              .readTimeout(30, TimeUnit.SECONDS)
          val interceptor = HttpLoggingInterceptor()
          interceptor.level = HttpLoggingInterceptor.Level.BODY
          client.addNetworkInterceptor(interceptor)
          val tlsSupportingClient = enableTls12On(client = client, logger = logger)
          return tlsSupportingClient.build()
      }*/

    /*@Provides
    @Singleton
    fun providesMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(context: Context): AppSharedPrefs {
        return AppSharedPrefs(context)
    }*/
}

/*  @Provides
 @Singleton
 fun providesTrustManagerFactory(context: Context): TrustManagerFactory {
     return createTrustManager(context)
 }*/