package com.veit.app.weatherino.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import com.veit.app.weatherino.api.current_weather.CurrentWeather
import com.veit.app.weatherino.databinding.CurrentWeatherViewBinding

class CurrentWeatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val binding = CurrentWeatherViewBinding.inflate(LayoutInflater.from(context), this, true)

    var loading = true
    set(value) {
        binding.loading = value
        field = value
    }

    var currentWeather: CurrentWeather? = null
    set(value) {
        binding.currentWeather = value
        field = value
    }
}

@BindingAdapter(value = ["weather", "loading"], requireAll = true)
fun CurrentWeatherView.bindWeather(weather: CurrentWeather?, loading: Boolean?) {
    if(loading == false) {
        this.loading = false
        this.currentWeather = weather
    } else {
        this.loading = true
    }
}