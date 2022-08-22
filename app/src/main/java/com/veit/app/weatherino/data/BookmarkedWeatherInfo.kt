package com.veit.app.weatherino.data

import com.veit.app.weatherino.api.daily_weather.DailyWeather
import com.veit.app.weatherino.data.db.WeatherBookmark

data class BookmarkedWeatherInfo(
    val bookmark: WeatherBookmark,
    val weather: DailyWeather
)
