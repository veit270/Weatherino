package com.veit.app.weatherino.api.current_weather


import com.google.gson.annotations.SerializedName
import com.veit.app.weatherino.data.TempData

data class Main(
    @SerializedName("feels_like")
    val feelsLike: TempData,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("temp")
    val temp: TempData,
    @SerializedName("temp_max")
    val tempMax: TempData,
    @SerializedName("temp_min")
    val tempMin: TempData
)