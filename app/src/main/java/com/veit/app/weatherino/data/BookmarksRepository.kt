package com.veit.app.weatherino.data

import com.veit.app.weatherino.data.db.BookmarksDao
import com.veit.app.weatherino.data.db.WeatherBookmark
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

interface BookmarksRepository {
    val bookmarksFlow: Flow<List<WeatherBookmark>>
    suspend fun deleteOldBookmarks()
    suspend fun addBookmark(weatherBookmark: WeatherBookmark)
    suspend fun deleteBookmark(bookmark: WeatherBookmark)
}

class BookmarksRepositoryImpl(private val bookmarksDao: BookmarksDao) : BookmarksRepository {

    override val bookmarksFlow = bookmarksDao.getAll()

    override suspend fun deleteOldBookmarks() {
        val today = LocalDate.now().minusDays(1)
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
        bookmarksDao.deleteOlderBookmarks(today)
    }

    override suspend fun addBookmark(weatherBookmark: WeatherBookmark) {
        if(bookmarksDao.exists(weatherBookmark.dateTsSeconds)) {
            bookmarksDao.update(weatherBookmark)
        } else {
            bookmarksDao.insert(weatherBookmark)
        }
    }

    override suspend fun deleteBookmark(bookmark: WeatherBookmark) {
        bookmarksDao.delete(bookmark)
    }
}