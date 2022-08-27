package com.veit.app.weatherino.viewmodel

import com.veit.app.weatherino.api.Coord
import com.veit.app.weatherino.api.Weather
import com.veit.app.weatherino.api.current_weather.*
import com.veit.app.weatherino.api.daily_weather.DailyWeather
import com.veit.app.weatherino.api.daily_weather.FeelsLike
import com.veit.app.weatherino.api.daily_weather.Temp
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.data.BookmarksRepository
import com.veit.app.weatherino.data.TempData
import com.veit.app.weatherino.data.db.WeatherBookmark
import com.veit.app.weatherino.ui.main.MainViewModel
import com.veit.app.weatherino.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest: CoroutinesRuleTest() {

    private lateinit var viewModel: MainViewModel

    @Mock
    lateinit var weatherChecker: WeatherChecker

    @Mock
    lateinit var bookmarksRepository: BookmarksRepository

    @Mock
    lateinit var locationProvider: LocationProvider

    private var currentWeather: CurrentWeather = prepareCurrentWeather()

    private val bookmarks = listOf(
        WeatherBookmark(123, "bookmark1"),
        WeatherBookmark(123, null),
        WeatherBookmark(123, "bookmark2"),
    )

    private val bookmarkedWeatherInfoList = bookmarks.map {
        BookmarkedWeatherInfo(it, prepareDailyWeather())
    }

    @Test
    fun loadCurrentWeather() = runTest {
        initViewModel()

        currentWeather = prepareCurrentWeather() // different dt
        `when`(weatherChecker.fetchCurrentWeather())
            .thenReturn(currentWeather)

        viewModel.loadCurrentWeather()

        viewModel.currentWeather.awaitValue(Resource.Success(currentWeather))
    }

    @Test
    fun loadBookmarks() = runTest {
        initViewModel()

        val bookmarksSubList = bookmarks.subList(1, 2)
        val bookmarkedWeatherInfoSubList = bookmarkedWeatherInfoList.subList(1, 2)

        `when`(bookmarksRepository.bookmarksFlow)
            .thenReturn(flowOf(bookmarksSubList))
        `when`(weatherChecker.fetchWeatherForBookmarks(bookmarksSubList))
            .thenReturn(bookmarkedWeatherInfoSubList)

        viewModel.loadBookmarks()

        viewModel.bookmarkedWeathers.awaitValue(Resource.Success(bookmarkedWeatherInfoSubList))
    }

    @Test
    fun addBookmark() = runTest {
        initViewModel()

        viewModel.addBookmark("name", 123)
        advanceUntilIdle()
    }

    @Test
    fun addBookmarkWithEmptyName() = runTest {
        initViewModel()

        viewModel.addBookmark("  ", 123)
        advanceUntilIdle()

        verify(bookmarksRepository).addBookmark(WeatherBookmark(123, null))
    }

    @Test
    fun deleteBookmark() = runTest {
        initViewModel()
        val bookmark = WeatherBookmark(123, "name")

        viewModel.deleteBookmark(BookmarkedWeatherInfo(bookmark, prepareDailyWeather()))
        advanceUntilIdle()

        verify(bookmarksRepository).deleteBookmark(bookmark)
    }

    private suspend fun initViewModel() {
        expectInit()
        viewModel = MainViewModel(weatherChecker, bookmarksRepository, locationProvider)
        assertInitValues()
    }

    private suspend fun expectInit() {
        `when`(weatherChecker.fetchCurrentWeather())
            .thenReturn(currentWeather)
        `when`(bookmarksRepository.bookmarksFlow)
            .thenReturn(flowOf(bookmarks))
        `when`(weatherChecker.fetchWeatherForBookmarks(bookmarks))
            .thenReturn(bookmarkedWeatherInfoList)
        `when`(locationProvider.locationAvailableFlow).thenReturn(flowOf(true))
    }

    private fun assertInitValues() {
        viewModel.currentWeather.awaitValue(Resource.Success(currentWeather))
        viewModel.bookmarkedWeathers.awaitValue(Resource.Success(bookmarkedWeatherInfoList))
    }

    private fun prepareDailyWeather(): DailyWeather {
        return DailyWeather(
            0, 0, 1,
            FeelsLike(
                TempData(20.0, ""), TempData(20.0, ""), TempData(20.0, ""), TempData(20.0, ""),
            ),
            2.0, 1, 2.0, 10, 1.0, 3.0, 5, 10,
            Temp(
                TempData(20.0, ""),
                TempData(20.0, ""),
                TempData(20.0, ""),
                TempData(20.0, ""),
                TempData(20.0, ""),
                TempData(20.0, ""),
            ),
            Weather("weather description", "01d", 5, "Rain"),
        )
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
