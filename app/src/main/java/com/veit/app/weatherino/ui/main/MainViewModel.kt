package com.veit.app.weatherino.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veit.app.weatherino.api.current_weather.CurrentWeather
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.data.BookmarksRepository
import com.veit.app.weatherino.data.db.WeatherBookmark
import com.veit.app.weatherino.utils.LocationProvider
import com.veit.app.weatherino.utils.Resource
import com.veit.app.weatherino.utils.WeatherChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherChecker: WeatherChecker,
    private val bookmarksRepository: BookmarksRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {
    private val _currentWeather = MutableLiveData<Resource<CurrentWeather>>(Resource.Loading())
    val currentWeather: LiveData<Resource<CurrentWeather>> = _currentWeather

    private val _bookmarkedWeathers = MutableLiveData<Resource<List<BookmarkedWeatherInfo>>>(Resource.Loading())
    val bookmarkedWeathers: LiveData<Resource<List<BookmarkedWeatherInfo>>> = _bookmarkedWeathers

    private var currentWeatherJob: Job? = null
    private var bookmarksJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.Default) {
            coroutineScope {
                locationProvider.locationAvailableFlow.collect {
                    if(it) {
                        coroutineContext.cancel()
                        refreshAll()
                    }
                }
            }
        }
    }

    fun refreshAll() {
        loadCurrentWeather()
        loadBookmarks()
    }

    fun loadCurrentWeather() {
        _currentWeather.postValue(Resource.Loading())
        currentWeatherJob?.cancel()
        currentWeatherJob = viewModelScope.launch(Dispatchers.Default) {
            _currentWeather.postValue(
                weatherChecker.fetchCurrentWeather()?.let { Resource.Success(it) }
                    ?: Resource.Error())
        }
    }

    fun loadBookmarks() {
        _currentWeather.postValue(Resource.Loading())
        bookmarksJob?.cancel()
        bookmarksJob = viewModelScope.launch(Dispatchers.Default) {
            bookmarksRepository.deleteOldBookmarks()
            bookmarksRepository.bookmarksFlow
                .collectLatest { bookmarks ->
                    if(bookmarks.isNotEmpty()) {
                        _bookmarkedWeathers.postValue(
                            weatherChecker.fetchWeatherForBookmarks(bookmarks)?.let {
                                Resource.Success(it)
                            } ?: Resource.Error()
                        )
                    } else {
                        _bookmarkedWeathers.postValue(Resource.Success(emptyList()))
                    }
                }
        }
    }

    fun addBookmark(name: String, time: Long) {
        viewModelScope.launch {
            bookmarksRepository.addBookmark(WeatherBookmark(time, name.ifBlank { null }))
        }
    }

    fun deleteBookmark(bookmarkedWeatherInfo: BookmarkedWeatherInfo) {
        viewModelScope.launch {
            bookmarksRepository.deleteBookmark(bookmarkedWeatherInfo.bookmark)
        }
    }
}