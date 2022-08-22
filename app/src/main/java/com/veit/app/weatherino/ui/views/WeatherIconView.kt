package com.veit.app.weatherino.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.veit.app.weatherino.databinding.WeatherIconViewBinding

class WeatherIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val binding = WeatherIconViewBinding.inflate(LayoutInflater.from(context), this, true)
}

@BindingAdapter("iconCode")
fun WeatherIconView.setIcon(code: String?) {
    code?.let {
        Glide.with(this)
            .load(getIconUrl(it))
            .fitCenter()
            .into(binding.imageView)
    } ?: binding.imageView.setImageDrawable(null)
}

fun getIconUrl(code: String) = "https://openweathermap.org/img/wn/$code@2x.png"