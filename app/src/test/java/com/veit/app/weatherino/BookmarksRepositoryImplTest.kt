package com.veit.app.weatherino

import com.veit.app.weatherino.data.BookmarksRepositoryImpl
import com.veit.app.weatherino.data.db.BookmarksDao
import com.veit.app.weatherino.data.db.WeatherBookmark
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BookmarksRepositoryImplTest {
    @Mock
    lateinit var bookmarksDao: BookmarksDao

    private lateinit var repository: BookmarksRepositoryImpl

    private val bookmark = WeatherBookmark(123, "bookmark1")

    private val flow = flowOf(listOf(bookmark))

    @Before
    fun setup() {
        `when`(bookmarksDao.getAll()).thenReturn(flow)
        repository = BookmarksRepositoryImpl(bookmarksDao)
    }

    @Test
    fun getBookmarksFlow() {
        assertEquals(flow, repository.bookmarksFlow)
    }

    @Test
    fun deleteOlderBookmarks() = runTest {
        repository.deleteOldBookmarks()
        verify(bookmarksDao).deleteOlderBookmarks(
            LocalDate.now().minusDays(1)
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toEpochSecond())
    }

    @Test
    fun deleteBookmark() = runTest {
        repository.deleteBookmark(bookmark)
        verify(bookmarksDao).delete(bookmark)
    }

    @Test
    fun addBookmarkWhenItExists() = runTest {
        `when`(bookmarksDao.exists(bookmark.dateTsSeconds))
            .thenReturn(true)

        repository.addBookmark(bookmark)
        verify(bookmarksDao).update(bookmark)
    }

    @Test
    fun addBookmarkWhenItNotExists() = runTest {
        `when`(bookmarksDao.exists(bookmark.dateTsSeconds))
            .thenReturn(false)

        repository.addBookmark(bookmark)
        verify(bookmarksDao).insert(bookmark)
    }
}