package com.veit.app.weatherino.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veit.app.weatherino.api.current_weather.CurrentWeather
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.data.BookmarksRepository
import com.veit.app.weatherino.utils.Resource
import com.veit.app.weatherino.utils.WeatherChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherChecker: WeatherChecker,
    private val bookmarksRepository: BookmarksRepository
) : ViewModel() {
    private val _currentWeather = MutableLiveData<Resource<CurrentWeather>>()
    val currentWeather: LiveData<Resource<CurrentWeather>> = _currentWeather

    private val _bookmarkedWeathers = MutableLiveData<Resource<List<BookmarkedWeatherInfo>>>()
    val bookmarkedWeathers: LiveData<Resource<List<BookmarkedWeatherInfo>>> = _bookmarkedWeathers

    private var currentWeatherJob: Job? = null
    private var bookmarksJob: Job? = null

    init {
        refreshAll()
    }

    fun refreshAll() {
        loadCurrentWeather()
        loadBookmarks()
    }

    fun loadCurrentWeather() {
        currentWeatherJob?.cancel()
        _currentWeather.value = Resource.Loading()
        currentWeatherJob = viewModelScope.launch {
            _currentWeather.value = weatherChecker.fetchCurrentWeather()?.let {
                Resource.Success(it)
            } ?: Resource.Error()
        }
    }

    fun loadBookmarks() {
        bookmarksJob?.cancel()
        bookmarksJob = viewModelScope.launch {
            val bookmarks = bookmarksRepository.fetchBookmarks()
            if(bookmarks.isNotEmpty()) {
                _bookmarkedWeathers.value = weatherChecker.fetchWeatherForBookmarks(bookmarks)?.let {
                    Resource.Success(it)
                } ?: Resource.Error()
            } else {
                _bookmarkedWeathers.value = Resource.Success(emptyList())
            }
        }
    }
}