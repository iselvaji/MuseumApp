package com.qardio.museum.data

import android.content.SharedPreferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.qardio.museum.data.local.database.ArtDatabase
import com.qardio.museum.data.remote.MuseumApiService
import com.qardio.museum.model.ArtObjects
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementation that uses a database backed [androidx.paging.PagingSource] and
 * [androidx.paging.RemoteMediator] to load pages from network when there are no more items cached
 * in the database to load.
 */

@Singleton
class ArtRepository @Inject constructor(
    private val preferences: SharedPreferences,
    private val apiService: MuseumApiService,
    private val appDatabase : ArtDatabase) {

    @OptIn(ExperimentalPagingApi::class)
    fun getArts(): Flow<PagingData<ArtObjects>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE, enablePlaceholders = true, prefetchDistance = PRE_FETCH_DISTANCE),
            remoteMediator = ArtMediator(appDatabase, apiService, preferences),
            pagingSourceFactory = { appDatabase.artDao().observeArtsPaginated()}
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 5
        const val PRE_FETCH_DISTANCE = 2
    }
}