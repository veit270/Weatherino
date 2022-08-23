package com.veit.app.weatherino.di

import com.google.gson.GsonBuilder
import com.veit.app.weatherino.api.WeatherApi
import com.veit.app.weatherino.data.BookmarksRepository
import com.veit.app.weatherino.data.BookmarksRepositoryImpl
import com.veit.app.weatherino.data.TempData
import com.veit.app.weatherino.data.db.BookmarksDao
import com.veit.app.weatherino.utils.TempDataAdapter
import com.veit.app.weatherino.utils.WeatherChecker
import com.veit.app.weatherino.utils.WeatherCheckerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .client(OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                    val originalHttpUrl = chain.request().url
                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("appid", "7f60d1bee88dc43268cdeb629925f8aa")
                        .addQueryParameter("units", "metric")
                        .build()
                    request.url(url)
                    return@addInterceptor chain.proceed(request.build())
                }
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .build()
            )
            .build()
    }

    private fun createGson() = GsonBuilder()
        .registerTypeAdapter(TempData::class.java, TempDataAdapter())
        .create()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}