package com.qardio.museum.database

import android.content.Context
import androidx.room.Room
import com.qardio.museum.data.local.database.ArtDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

/**
 * Module which create and provide Room inMemory Database Builder
 *
 */

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context, ArtDatabase::class.java
        ).allowMainThreadQueries()
            .build()

}
