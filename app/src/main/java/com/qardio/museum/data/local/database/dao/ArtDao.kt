package com.qardio.museum.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qardio.museum.model.ArtObjects
import com.qardio.museum.utils.Constants
import kotlinx.coroutines.flow.Flow

/**
 * DAO which has Database methods to read, write, delete art objects
 * Used as a Paging Source for local storage operations
 */
@Dao
interface ArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(art: ArtObjects)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(arts: List<ArtObjects>)

    @Query("SELECT * FROM ${Constants.DBConstants.TABLE_ART}")
    fun observeArtsPaginated(): PagingSource<Int, ArtObjects>

    @Query("SELECT * FROM ${Constants.DBConstants.TABLE_ART}")
    fun getArtData(): Flow<List<ArtObjects>>

    @Query("DELETE FROM ${Constants.DBConstants.TABLE_ART}")
    suspend fun clearAll()

}