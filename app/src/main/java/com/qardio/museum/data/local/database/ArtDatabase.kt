package com.qardio.museum.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qardio.museum.data.local.database.entity.RemoteKeys
import com.qardio.museum.data.local.database.dao.ArtDao
import com.qardio.museum.data.local.database.dao.RemoteKeyDao
import com.qardio.museum.model.ArtObjects

@Database(entities = [ArtObjects::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}