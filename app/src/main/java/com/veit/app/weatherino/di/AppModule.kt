package com.veit.app.weatherino.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.veit.app.weatherino.api.Weather
import com.veit.app.weatherino.api.WeatherApi
import com.veit.app.weatherino.api.WeatherApiRequestInterceptor
import com.veit.app.weatherino.data.BookmarksRepository
import com.veit.app.weatherino.data.BookmarksRepositoryImpl
import com.veit.app.weatherino.data.TempData
import com.veit.app.weatherino.data.db.BookmarksDao
import com.veit.app.weatherino.utils.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherChecker(weatherApi: WeatherApi): WeatherChecker {
        return WeatherCheckerImpl(weatherApi)
    }

    @Provides
    @Singleton
    fun provideBookmarksRepository(bookmarksDao: BookmarksDao): BookmarksRepository {
        return BookmarksRepositoryImpl(bookmarksDao)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @com.veit.app.weatherino.di.RequestInterceptorQualifier requestInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(10))
            .callTimeout(Duration.ofSeconds(20))
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @com.veit.app.weatherino.di.RequestInterceptorQualifier
    fun provideRequestInterceptor(locationProvider: LocationProvider, settingsManager: SettingsManager): Interceptor {
        return WeatherApiRequestInterceptor(locationProvider, settingsManager)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    }

    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext context: Context): LocationProvider {
        return LocationProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideSettingsManager(@ApplicationContext context: Context): SettingsManager {
        return SettingsManagerImpl(context)
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTempDataAdapter(settingsManager: SettingsManager): TempDataAdapter {
        return TempDataAdapter(settingsManager)
    }

    @Provides
    @Singleton
    fun provideGson(tempDataAdapter: TempDataAdapter): Gson {
        return GsonBuilder()
            .registerTypeAdapter(TempData::class.java, tempDataAdapter)
            .registerTypeAdapter(Weather::class.java, WeatherDeserializer())
            .create()
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestInterceptorQualifier