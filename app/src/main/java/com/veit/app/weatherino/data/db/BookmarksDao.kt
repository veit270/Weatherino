package com.veit.app.weatherino.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM WeatherBookmark ORDER BY dateTs DESC")
    suspend fun getAll(): List<WeatherBookmark>

    @Delete
    suspend fun delete(bookmark: WeatherBookmark)

    @Query("DELETE FROM WeatherBookmark WHERE dateTs < :dateTs")
    suspend fun deleteOlderBookmarks(dateTs: Long)
}