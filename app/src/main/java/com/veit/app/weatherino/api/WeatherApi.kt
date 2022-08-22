package com.veit.app.weatherino.api

import com.veit.app.weatherino.api.current_weather.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Call<CurrentWeather>
}