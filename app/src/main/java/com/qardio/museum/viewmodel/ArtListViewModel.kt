package com.qardio.museum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.qardio.museum.data.ArtRepository
import com.qardio.museum.data.NetworkStatusRepository
import com.qardio.museum.model.ArtObjects
import com.qardio.museum.model.NetworkStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel for the ArtListFragment screen.
 * The ViewModel works with the [ArtRepository] to get the data.
 */

@HiltViewModel
class ArtListViewModel @Inject constructor (
    private val networkStatusRepository: NetworkStatusRepository,
    private val repository: ArtRepository,
    application: Application
) : AndroidViewModel(application) {

    val networkState: StateFlow<NetworkStatusState> = networkStatusRepository.state

    fun isDeviceOnline() : Boolean = networkStatusRepository.hasNetworkConnection()

    fun fetchArts(): Flow<PagingData<ArtObjects>> {
        return repository.getArts().cachedIn(viewModelScope)
    }
}