package com.veit.app.weatherino.utils

import android.content.Context
import android.text.format.DateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

object Utils {
    /**
     * Converts given time in seconds to readable date based on the context's locale, e.g. 8/22/2022
     *
     * @param seconds seconds
     * @param context context
     * @return readable date
     */
    fun secondsToReadableDate(seconds: Long, context: Context): String = DateFormat.getDateFormat(context)
        .format(Date(seconds * 1000))
}

fun toReadableTemperature(temp: Double, unitChar: Char) = "${temp.toInt()} $unitChar"

/**
 * Converts given time in seconds to time of start of day in the default timezone.
 * So if given time points to 02.04.2022 21:37 GMT+2, then the returned time will point to 02.04.2022 00:00 GMT+2.
 *
 * @param time time in seconds
 * @return time of start of day in seconds
 */
fun convertToStartOfDay(time: Long): Long {
    return Instant.ofEpochSecond(time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toEpochSecond()
}