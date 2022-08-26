package com.veit.app.weatherino

import com.veit.app.weatherino.data.BookmarksRepositoryImpl
import com.veit.app.weatherino.data.db.BookmarksDao
import com.veit.app.weatherino.data.db.WeatherBookmark
import com.veit.app.weatherino.utils.convertToStartOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class BookmarksRepositoryImplTest {
    @Mock
    lateinit var bookmarksDao: BookmarksDao

    private lateinit var repository: BookmarksRepositoryImpl

    val bookmark = WeatherBookmark(123, "bookmark1")

    private val flow = flowOf(listOf(bookmark))

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(bookmarksDao.getAll()).thenReturn(flow)
        repository = BookmarksRepositoryImpl(bookmarksDao)
    }

    @Test
    fun getBookmarksFlow() {
        assertEquals(flow, repository.bookmarksFlow)
    }

    @Test
    fun deleteOlderBookmarks() = runTest {
        repository.deleteOldBookmarks()
        Mockito.verify(bookmarksDao).deleteOlderBookmarks(convertToStartOfDay(Date().time))
    }

    @Test
    fun deleteBookmark() = runTest {
        repository.deleteBookmark(bookmark)
        Mockito.verify(bookmarksDao).delete(bookmark)
    }

    @Test
    fun addBookmarkWhenItExists() = runTest {
        Mockito.`when`(bookmarksDao.exists(bookmark.dateTsSeconds))
            .thenReturn(true)

        repository.addBookmark(bookmark)
        Mockito.verify(bookmarksDao).update(bookmark)
    }

    @Test
    fun addBookmarkWhenItNotExists() = runTest {
        Mockito.`when`(bookmarksDao.exists(bookmark.dateTsSeconds))
            .thenReturn(false)

        repository.addBookmark(bookmark)
        Mockito.verify(bookmarksDao).insert(bookmark)
    }
}