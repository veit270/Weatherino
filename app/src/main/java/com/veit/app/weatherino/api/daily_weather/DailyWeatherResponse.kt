package com.veit.app.weatherino.api.daily_weather


import com.google.gson.annotations.SerializedName

data class DailyWeatherResponse(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val list: List<DailyWeather>,
    @SerializedName("message")
    val message: Double
)