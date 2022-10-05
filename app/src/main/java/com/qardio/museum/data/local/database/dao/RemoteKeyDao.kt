package com.qardio.museum.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qardio.museum.data.local.database.entity.RemoteKeys

/**
 * Remote Key DAO which has methods to read, write, delete remote key objects
 * Used in Paging Remote Mediator to tell the backend service which data to load next.
 */

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remotekeys WHERE id = :id")
    suspend fun remoteKeys(id: String): RemoteKeys?

    @Query("DELETE FROM remotekeys")
    suspend fun clearRemoteKeys()
}