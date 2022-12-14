package com.veit.app.weatherino.api

import com.veit.app.weatherino.utils.LocationProvider
import com.veit.app.weatherino.utils.SettingsManager
import okhttp3.Interceptor
import okhttp3.Response

class WeatherApiRequestInterceptor(
    private val locationProvider: LocationProvider,
    private val settingsManager: SettingsManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val (lat, lon) = locationProvider.getLocation()
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("appid", "7f60d1bee88dc43268cdeb629925f8aa")
            .addQueryParameter("units", settingsManager.userSettings.temperatureUnit.apiName)
            .addQueryParameter("lat", lat.toString())
            .addQueryParameter("lon", lon.toString())
            .build()
        request.url(url)
        return chain.proceed(request.build())
    }
}