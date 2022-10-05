package com.qardio.museum.di

import android.content.Context
import androidx.room.Room
import com.qardio.museum.data.local.database.ArtDatabase
import com.qardio.museum.data.local.database.dao.ArtDao
import com.qardio.museum.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module used Hilt dependency injection for Database related classes
 * It provides the instance of DAO, Database
 */

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDao(database: ArtDatabase): ArtDao {
        return database.artDao()
    }

    @Provides
    @Singleton
    fun provideArtDatabase(@ApplicationContext appContext: Context): ArtDatabase {
        return Room.databaseBuilder(appContext,
            ArtDatabase::class.java, Constants.DBConstants.DATABASE_NAME
        ).build()
    }

}