package com.veit.app.weatherino.di

import android.content.Context
import com.veit.app.weatherino.data.AppDatabase
import com.veit.app.weatherino.data.BookmarksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideBookmarksDao(appDatabase: AppDatabase): BookmarksDao {
        return appDatabase.bookmarksDao()
    }
}