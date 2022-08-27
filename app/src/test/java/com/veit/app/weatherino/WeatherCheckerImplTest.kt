package com.veit.app.weatherino

import com.veit.app.weatherino.api.Coord
import com.veit.app.weatherino.api.Weather
import com.veit.app.weatherino.api.WeatherApi
import com.veit.app.weatherino.api.current_weather.*
import com.veit.app.weatherino.data.TempData
import com.veit.app.weatherino.utils.CoroutinesRuleTest
import com.veit.app.weatherino.utils.WeatherCheckerImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherCheckerImplTest: CoroutinesRuleTest() {

    @Mock
    lateinit var weatherApi: WeatherApi

    private lateinit var weatherChecker: WeatherCheckerImpl

    @Before
    fun setup() {
        weatherChecker = WeatherCheckerImpl(weatherApi)
    }

    @Test
    fun checkCurrentWeatherWithSuccess() = runTest {
        val apiCall: Call<CurrentWeather> = mock(Call::class.java) as Call<CurrentWeather>
        val currentWeather = prepareCurrentWeather()

        `when`(weatherApi.getCurrentWeather()).thenReturn(apiCall)
        `when`(apiCall.execute()).thenReturn(Response.success(currentWeather))

        val fetchedCurrentWeather = weatherChecker.fetchCurrentWeather()
        advanceUntilIdle()

        assertEquals(currentWeather, fetchedCurrentWeather)
    }

    @Test
    fun checkCurrentWeatherWithFailure() = runTest {
        val apiCall: Call<CurrentWeather> = mock(Call::class.java) as Call<CurrentWeather>

        `when`(weatherApi.getCurrentWeather()).thenReturn(apiCall)
        `when`(apiCall.execute()).thenReturn(Response.error(403, EMPTY_RESPONSE))

        val fetchedCurrentWeather = weatherChecker.fetchCurrentWeather()
        advanceUntilIdle()

        assertEquals(null, fetchedCurrentWeather)
    }

    @Test
    fun checkCurrentWeatherWithException() = runTest {
        val apiCall: Call<CurrentWeather> = mock(Call::class.java) as Call<CurrentWeather>

        `when`(weatherApi.getCurrentWeather()).thenReturn(apiCall)
        `when`(apiCall.execute()).thenThrow(IOException())

        val fetchedCurrentWeather = weatherChecker.fetchCurrentWeather()
        advanceUntilIdle()

        assertEquals(null, fetchedCurrentWeather)
    }

    private fun prepareCurrentWeather(): CurrentWeather {
        return CurrentWeather(
            "",
            Clouds(0),
            0,
            Coord(23.0, 23.0),
            Date().time,
            1,
            Main(
                TempData(20.0, ""),
                10,
                10,
                TempData(20.0, ""),
                TempData(20.0, ""),
                TempData(20.0, "")
            ),
            "Name",
            Sys("EN", 1, 1, 1, 1),
            1800,
            10,
            Weather("weather description", "01d", 5, "Rain"),
            Wind(2, 2.0)
        )
    }

}