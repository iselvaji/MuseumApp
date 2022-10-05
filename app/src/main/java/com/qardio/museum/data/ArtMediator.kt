package com.qardio.museum.data

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.qardio.museum.data.local.database.ArtDatabase
import com.qardio.museum.data.local.database.entity.RemoteKeys
import com.qardio.museum.data.remote.MuseumApiService
import com.qardio.museum.model.ArtObjects
import com.qardio.museum.utils.Constants
import com.qardio.museum.utils.Constants.Companion.CREATOR_NAME_TO_QUERY
import com.qardio.museum.utils.Constants.Companion.DATA_REFRESH_INTERVAL
import com.qardio.museum.utils.Constants.Companion.DEFAULT_PAGE_INDEX
import com.qardio.museum.utils.PaginationUtils.isRefreshIntervalExpired
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Remote Mediator class used to manage the data between local and remote data sources
 * If data is available in database the fetch and display
 * also fetch the details from network an update the database on regular interval [DATA_REFRESH_INTERVAL]
 *
*/

@OptIn(ExperimentalPagingApi::class)
class ArtMediator @Inject constructor(
    private val appDatabase: ArtDatabase,
    private val apiService: MuseumApiService,
    private val preferences: SharedPreferences) :
    RemoteMediator<Int, ArtObjects>() {

    override suspend fun initialize(): InitializeAction {

        // retrieve the last updated time
        val lastUpdateTime = preferences.getLong(Constants.PREF_KEY_UPDATE_TIME, 0L)

        // check data refresh required
        val isRefreshRequired = isRefreshIntervalExpired(lastUpdateTime)

        Log.d("isRefreshRequired ", isRefreshRequired.toString())

        return if (isRefreshRequired) {
            // Refresh time interval expired, refresh cached data from network
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            // Cached data is up-to-date, so there is no need to re-fetch from network
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArtObjects>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)

        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            Log.d("page number : ", page.toString())

            // fetch updated data from api for next page
            val response = apiService.getArtsByCreator(CREATOR_NAME_TO_QUERY, page, state.config.pageSize)
            val isEndOfList = response.artObjects.isEmpty()

            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeyDao().clearRemoteKeys()
                    appDatabase.artDao().clearAll()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1

                val keys = response.artObjects.map {
                    RemoteKeys(id = it.artId, prevKey = prevKey, nextKey = nextKey)
                }

                // Insert new data into database, which invalidates the
                // current PagingData, allowing Paging to present the updates in the DB.
                appDatabase.remoteKeyDao().insertAll(keys)
                appDatabase.artDao().insertAll(response.artObjects)

                // save the last updated time to check the refresh [DATA_REFRESH_INTERVAL] interval
                preferences.edit().putLong(Constants.PREF_KEY_UPDATE_TIME, System.currentTimeMillis()).apply()
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ArtObjects>): Any {
        Log.d("loadType -> ", loadType.toString())
        return when (loadType) {
            LoadType.REFRESH -> {
                 val remoteKeys = getClosestRemoteKey(state)
                 remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
             }
             LoadType.APPEND -> {
                 val remoteKeys = getLastRemoteKey(state)
                 remoteKeys?.nextKey ?: DEFAULT_PAGE_INDEX
             }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, ArtObjects>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { art -> appDatabase.remoteKeyDao().remoteKeys(art.artId) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, ArtObjects>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { art -> appDatabase.remoteKeyDao().remoteKeys(art.artId) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, ArtObjects>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.artId?.let { id ->
                appDatabase.remoteKeyDao().remoteKeys(id)
            }
        }
    }

}
