package com.veit.app.weatherino.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veit.app.weatherino.api.current_weather.CurrentWeather
import com.veit.app.weatherino.data.BookmarkedWeatherInfo
import com.veit.app.weatherino.data.BookmarksRepository
import com.veit.app.weatherino.data.db.WeatherBookmark
import com.veit.app.weatherino.utils.Resource
import com.veit.app.weatherino.utils.WeatherChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherChecker: WeatherChecker,
    private val bookmarksRepository: BookmarksRepository
) : ViewModel() {
    private val _currentWeather = MutableLiveData<Resource<CurrentWeather>>(Resource.Loading())
    val currentWeather: LiveData<Resource<CurrentWeather>> = _currentWeather

    private val _bookmarkedWeathers = MutableLiveData<Resource<List<BookmarkedWeatherInfo>>>(Resource.Loading())
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
        _currentWeather.value = Resource.Loading()
        currentWeatherJob?.cancel()
        currentWeatherJob = viewModelScope.launch {
            _currentWeather.value = weatherChecker.fetchCurrentWeather()?.let {
                Resource.Success(it)
            } ?: Resource.Error()
        }
    }

    fun loadBookmarks() {
        _bookmarkedWeathers.value = Resource.Loading()
        bookmarksJob?.cancel()
        bookmarksJob = viewModelScope.launch {
            bookmarksRepository.deleteOldBookmarks()
            bookmarksRepository.bookmarksFlow
                .collectLatest { bookmarks ->
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