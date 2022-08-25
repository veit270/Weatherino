package com.veit.app.weatherino.api

import com.veit.app.weatherino.api.current_weather.CurrentWeather
import com.veit.app.weatherino.api.daily_weather.DailyWeatherResponse
import retrofit2.Call
import retrofit2.http.GET

interface WeatherApi {
    @GET("/data/2.5/weather")
    fun getCurrentWeather(): Call<CurrentWeather>

    @GET("/data/2.5/forecast/daily")
    fun getDailyWeather(): Call<DailyWeatherResponse>
}