package com.veit.app.weatherino.data

import com.veit.app.weatherino.data.db.BookmarksDao
import com.veit.app.weatherino.data.db.WeatherBookmark
import java.time.LocalDate
import java.time.ZoneId

interface BookmarksRepository {
    suspend fun fetchBookmarks(): List<WeatherBookmark>
}

class BookmarksRepositoryImpl(private val bookmarksDao: BookmarksDao) : BookmarksRepository {
    override suspend fun fetchBookmarks(): List<WeatherBookmark> {
        val today = LocalDate.now().minusDays(1)
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        bookmarksDao.deleteOlderBookmarks(today)
        return bookmarksDao.getAll()
    }
}