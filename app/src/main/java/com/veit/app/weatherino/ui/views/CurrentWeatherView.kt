package com.veit.app.weatherino.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import com.veit.app.weatherino.api.current_weather.CurrentWeather
import com.veit.app.weatherino.databinding.CurrentWeatherViewBinding
import com.veit.app.weatherino.utils.Resource
import com.veit.app.weatherino.utils.Status

class CurrentWeatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = CurrentWeatherViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setCurrentWeatherResource(currentWeatherResource: Resource<CurrentWeather>) {
        when(currentWeatherResource.status) {
            Status.SUCCESS -> {
                binding.loading = false
                binding.currentWeather = currentWeatherResource.data
            }
            Status.ERROR -> {
                binding.loading = false
                binding.currentWeather = null
            }
            Status.LOADING -> {
                binding.loading = true
                binding.currentWeather = null
            }
        }
    }
}

@BindingAdapter("currentWeatherResource")
fun CurrentWeatherView.bindCurrentWeatherResource(currentWeatherResource: Resource<CurrentWeather>) {
    setCurrentWeatherResource(currentWeatherResource)
}