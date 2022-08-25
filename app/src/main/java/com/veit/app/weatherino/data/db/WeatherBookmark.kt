package com.veit.app.weatherino.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherBookmark(
    @PrimaryKey
    val dateTsSeconds: Long,
    @ColumnInfo
    val name: String?
)
