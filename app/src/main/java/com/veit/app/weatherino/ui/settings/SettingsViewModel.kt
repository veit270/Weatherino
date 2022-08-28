package com.veit.app.weatherino.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.veit.app.weatherino.utils.SettingsManager
import com.veit.app.weatherino.utils.TemperatureUnitType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    settingsManager: SettingsManager
) : ViewModel() {
    private val userSettings = settingsManager.userSettings
    val temperatureTypeButtonChecked = MutableLiveData(userSettings.temperatureUnit.radioButtonId)

    init {
        temperatureTypeButtonChecked.observeForever {
            userSettings.temperatureUnit =
                TemperatureUnitType.values().find { type -> type.radioButtonId == it }
                    ?: TemperatureUnitType.CELSIUS
            settingsManager.userSettings = userSettings
        }
    }
}