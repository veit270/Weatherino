package com.veit.app.weatherino.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.veit.app.weatherino.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface SettingsManager {
    var userSettings: UserSettings
    val settingsUpdatedFlow: Flow<UserSettings>
}

class SettingsManagerImpl(context: Context): SettingsManager {
    private val gson = Gson()

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_DEFAULT, Context.MODE_PRIVATE)

    override val settingsUpdatedFlow: MutableStateFlow<UserSettings> = MutableStateFlow(userSettings)

    override var userSettings: UserSettings
        get() = sharedPreferences
            .getString(USER_SETTINGS_JSON, null)?.let {
                gson.fromJson(it, UserSettings::class.java)
            } ?: UserSettings()
        set(value) {
            sharedPreferences.edit()
                .putString(USER_SETTINGS_JSON, gson.toJson(value))
                .apply()
            settingsUpdatedFlow.value = userSettings
        }

    companion object {
        private const val SHARED_PREFERENCES_DEFAULT = "default"
        private const val USER_SETTINGS_JSON = "user settings"
    }
}

data class UserSettings(
    @SerializedName("temperature_unit")
    var temperatureUnit: TemperatureUnitType = TemperatureUnitType.CELSIUS
)


enum class TemperatureUnitType(val radioButtonId: Int, val apiName: String, val unitChar: Char) {
    CELSIUS(R.id.celsiusRadioButton, "metric", '\u2103'),
    FAHRENHEIT(R.id.fahrenheitRadioButton, "imperial", '\u2109'),
    KELVIN(R.id.kelvinRadioButton, "standard", '\u212A')
}