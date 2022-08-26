package com.veit.app.weatherino.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.veit.app.weatherino.utils.Resource
import com.veit.app.weatherino.utils.WeatherChecker
import com.veit.app.weatherino.utils.awaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Mock
    lateinit var weatherChecker: WeatherChecker

    @Mock
    lateinit var bookmarksRepository: BookmarksRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var currentWeather: CurrentWeather = prepareCurrentWeather()

    private val bookmarks = listOf(
        WeatherBookmark(123, "bookmark1"),
        WeatherBookmark(123, null),
        WeatherBookmark(123, "bookmark2"),
    )

    private val bookmarkedWeatherInfoList = bookmarks.map {
        BookmarkedWeatherInfo(it, prepareDailyWeather())
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadCurrentWeather() = runTest {
        initViewModel()

        currentWeather = prepareCurrentWeather() // different dt
        Mockito.`when`(weatherChecker.fetchCurrentWeather())
            .thenReturn(currentWeather)

        viewModel.loadCurrentWeather()

        viewModel.currentWeather.awaitValue(Resource.Success(currentWeather))
    }

    @Test
    fun loadBookmarks() = runTest {
        initViewModel()

        val bookmarksSubList = bookmarks.subList(1, 2)
        val bookmarkedWeatherInfoSubList = bookmarkedWeatherInfoList.subList(1, 2)

        Mockito.`when`(bookmarksRepository.bookmarksFlow)
            .thenReturn(flowOf(bookmarksSubList))
        Mockito.`when`(weatherChecker.fetchWeatherForBookmarks(bookmarksSubList))
            .thenReturn(bookmarkedWeatherInfoSubList)

        viewModel.loadBookmarks()

        viewModel.bookmarkedWeathers.awaitValue(Resource.Success(bookmarkedWeatherInfoSubList))
    }

    @Test
    fun addBookmark() = runTest {
        initViewModel()

        Mockito.`when`(bookmarksRepository.addBookmark(WeatherBookmark(123, "name")))
        viewModel.addBookmark("name", 123)
    }

    @Test
    fun deleteBookmark() = runTest {
        initViewModel()

        val bookmark = WeatherBookmark(123, "name")
        Mockito.`when`(bookmarksRepository.deleteBookmark(bookmark))
        viewModel.deleteBookmark(BookmarkedWeatherInfo(bookmark, prepareDailyWeather()))
    }

    private suspend fun initViewModel() {
        expectInit()
        viewModel = MainViewModel(weatherChecker, bookmarksRepository)
        assertInitValues()
    }

    private suspend fun expectInit() {
        Mockito.`when`(weatherChecker.fetchCurrentWeather())
            .thenReturn(currentWeather)
        Mockito.`when`(bookmarksRepository.bookmarksFlow)
            .thenReturn(flowOf(bookmarks))
        Mockito.`when`(weatherChecker.fetchWeatherForBookmarks(bookmarks))
            .thenReturn(bookmarkedWeatherInfoList)
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
