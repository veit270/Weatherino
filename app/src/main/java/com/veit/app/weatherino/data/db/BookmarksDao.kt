package com.veit.app.weatherino.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM WeatherBookmark ORDER BY dateTsSeconds ASC")
    fun getAll(): Flow<List<WeatherBookmark>>

    @Delete
    suspend fun delete(bookmark: WeatherBookmark)

    @Update
    suspend fun update(bookmark: WeatherBookmark)

    @Insert
    suspend fun insert(bookmark: WeatherBookmark)

    @Query("SELECT EXISTS (SELECT 1 FROM WeatherBookmark WHERE dateTsSeconds = :dateTs)")
    suspend fun exists(dateTs: Long): Boolean

    @Query("DELETE FROM WeatherBookmark WHERE dateTsSeconds < :dateTs")
    suspend fun deleteOlderBookmarks(dateTs: Long)
}