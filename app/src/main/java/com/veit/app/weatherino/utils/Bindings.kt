package com.veit.app.weatherino.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.bindVisibleOrGone(visible: Boolean?) {
    visibleOrGone(visible ?: false)
}

@BindingAdapter("visibleOrInvisible")
fun View.bindVisibleOrInvisible(visible: Boolean?) {
    visibleOrInvisible(visible ?: false)
}