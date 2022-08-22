package com.veit.app.weatherino.utils

import com.veit.app.weatherino.api.WeatherApi
import com.veit.app.weatherino.api.current_weather.CurrentWeather
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.data.db.WeatherBookmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

interface WeatherChecker {
    suspend fun fetchCurrentWeather(): CurrentWeather?
    fun fetchWeatherForBookmarks(bookmarks: List<WeatherBookmark>): List<BookmarkedWeatherInfo>?
}

class WeatherCheckerImpl @Inject constructor(
    private val weatherApi: WeatherApi
): WeatherChecker {
    override suspend fun fetchCurrentWeather(): CurrentWeather? {
        return withContext(Dispatchers.IO) {
            try {
                weatherApi.getCurrentWeather("23", "23").execute().let {
                    if(it.isSuccessful) it.body() else null
                }
            } catch (e: IOException) {
                null
            }
        }
    }

    override fun fetchWeatherForBookmarks(bookmarks: List<WeatherBookmark>): List<BookmarkedWeatherInfo>? {
        TODO("Not yet implemented")
    }
}