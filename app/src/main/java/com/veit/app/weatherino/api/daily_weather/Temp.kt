package com.veit.app.weatherino.api.daily_weather


import com.google.gson.annotations.SerializedName
import com.veit.app.weatherino.data.TempData

data class Temp(
    @SerializedName("day")
    val day: TempData,
    @SerializedName("eve")
    val eve: TempData,
    @SerializedName("max")
    val max: TempData,
    @SerializedName("min")
    val min: TempData,
    @SerializedName("morn")
    val morn: TempData,
    @SerializedName("night")
    val night: TempData
)