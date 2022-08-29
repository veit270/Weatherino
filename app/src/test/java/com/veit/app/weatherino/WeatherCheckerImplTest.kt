package com.veit.app.weatherino

import com.veit.app.weatherino.api.Coord
import com.veit.app.weatherino.api.Weather
import com.veit.app.weatherino.api.WeatherApi
import com.veit.app.weatherino.api.current_weather.*
import com.veit.app.weatherino.api.daily_weather.*
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.data.TempData
import com.veit.app.weatherino.data.db.WeatherBookmark
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
import java.time.LocalDateTime
import java.time.ZoneId
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
        val apiCall: Call<CurrentWeather> = mockApiCall()

        `when`(weatherApi.getCurrentWeather()).thenReturn(apiCall)
        `when`(apiCall.execute()).thenReturn(Response.error(403, EMPTY_RESPONSE))

        val fetchedCurrentWeather = weatherChecker.fetchCurrentWeather()
        advanceUntilIdle()

        assertEquals(null, fetchedCurrentWeather)
    }

    @Test
    fun checkCurrentWeatherWithException() = runTest {
        val apiCall: Call<CurrentWeather> = mockApiCall()

        `when`(weatherApi.getCurrentWeather()).thenReturn(apiCall)
        `when`(apiCall.execute()).thenThrow(IOException())

        val fetchedCurrentWeather = weatherChecker.fetchCurrentWeather()
        advanceUntilIdle()

        assertEquals(null, fetchedCurrentWeather)
    }

    @Test
    fun fetchWeatherForBookmarksWithSuccess() = runTest {
        val apiCall: Call<DailyWeatherResponse> = mockApiCall()

        `when`(weatherApi.getDailyWeather()).thenReturn(apiCall)
        val weatherResponse = createDailyWeatherResponse()
        `when`(apiCall.execute()).thenReturn(Response.success(weatherResponse))

        val bookmarks = listOf(
            WeatherBookmark(getDateTime(0, true), ""),
            WeatherBookmark(getDateTime(2, true), ""),
            WeatherBookmark(getDateTime(3, true), ""),
        )
        val fetchedCurrentWeather = weatherChecker.fetchWeatherForBookmarks(bookmarks)
        advanceUntilIdle()

        assertEquals(listOf(
            BookmarkedWeatherInfo(bookmarks[0], weatherResponse.list[0]),
            BookmarkedWeatherInfo(bookmarks[1], weatherResponse.list[2]),
            BookmarkedWeatherInfo(bookmarks[2], null),
        ), fetchedCurrentWeather)
    }

    private fun createDailyWeatherResponse(): DailyWeatherResponse {
        return DailyWeatherResponse(
            City(Coord(1.0, 1.0), "EN", 1, "name", 200, 1800),
            0, "cod",
            listOf(
                prepareDailyWeather(0),
                prepareDailyWeather(1),
                prepareDailyWeather(2)
            ), 2.0
        )
    }

    private fun prepareDailyWeather(daysOffset: Long) = DailyWeather(
        0, 0, getDateTime(daysOffset), FeelsLike(
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0")
        ), 0.1, 1, 1.0, 1, 1.0, 2.0, 1, 2,
        Temp(
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0"),
            TempData(20.0, "21.0"),
        ),
        Weather("weather description", "01d", 5, "Rain")
    )

    private fun getDateTime(daysOffset: Long, startOfDay: Boolean = false): Long {
        return LocalDateTime.now()
            .let { if(startOfDay) it.toLocalDate().atStartOfDay() else it }
            .plusDays(daysOffset)
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
    }

    @Test
    fun fetchWeatherForBookmarksWithFailure() = runTest {
        val apiCall: Call<DailyWeatherResponse> = mockApiCall()

        `when`(weatherApi.getDailyWeather()).thenReturn(apiCall)
        `when`(apiCall.execute()).thenReturn(Response.error(403, EMPTY_RESPONSE))

        val fetchedCurrentWeather = weatherChecker.fetchWeatherForBookmarks(emptyList())
        advanceUntilIdle()

        assertEquals(null, fetchedCurrentWeather)
    }

    @Test
    fun fetchWeatherForBookmarksWithException() = runTest {
        val apiCall: Call<DailyWeatherResponse> = mockApiCall()

        `when`(weatherApi.getDailyWeather()).thenReturn(apiCall)
        `when`(apiCall.execute()).thenThrow(IOException())

        val fetchedWeatherForBookmarks = weatherChecker.fetchWeatherForBookmarks(emptyList())
        advanceUntilIdle()

        assertEquals(null, fetchedWeatherForBookmarks)
    }

    private fun <T> mockApiCall(): T = mock(Call::class.java) as T

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