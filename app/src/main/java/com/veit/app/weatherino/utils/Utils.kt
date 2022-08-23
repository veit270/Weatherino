package com.veit.app.weatherino.utils

import android.content.Context
import android.text.format.DateFormat
import java.util.*

object Utils {
    fun toReadableDate(dateTs: Long, context: Context): String = DateFormat.getDateFormat(context)
        .format(Date(dateTs))

    fun toReadableTemperature(temp: Double) = "${temp.toInt()} \u2103"
}