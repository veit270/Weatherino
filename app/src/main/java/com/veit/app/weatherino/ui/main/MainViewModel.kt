package com.veit.app.weatherino.ui.main

import androidx.lifecycle.ViewModel
import com.veit.app.weatherino.api.WeatherApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherApi: WeatherApi
) : ViewModel() {

}