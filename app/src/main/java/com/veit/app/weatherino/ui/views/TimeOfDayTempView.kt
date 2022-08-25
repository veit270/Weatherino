package com.veit.app.weatherino.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import com.veit.app.weatherino.databinding.TimeOfDayTempViewBinding

class TimeOfDayTempView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = TimeOfDayTempViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setData(temp: String?, feelsLike: String?, image: Drawable?) {
        with(binding) {
            this.temp = temp
            this.feelsLike = feelsLike
            this.image = image
        }
    }
}

@BindingAdapter(value = ["temp", "feelsLike", "image"], requireAll = true)
fun TimeOfDayTempView.bindData(temp: String?, feelsLike: String?, image: Drawable?) {
    setData(temp, feelsLike, image)
}