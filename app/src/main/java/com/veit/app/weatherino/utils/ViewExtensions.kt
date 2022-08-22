package com.veit.app.weatherino.utils

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visibleOrGone(visible: Boolean) {
    if(visible) visible() else gone()
}

fun View.visibleOrInvisible(visible: Boolean) {
    if(visible) visible() else invisible()
}
