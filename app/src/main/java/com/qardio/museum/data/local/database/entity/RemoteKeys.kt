package com.qardio.museum.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Remote Key Database entity which contains id, prevKey and nextKey
 * Used in Paging Remote Mediator to tell the backend service which data to load next.
 */

@Entity
data class RemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
    )
