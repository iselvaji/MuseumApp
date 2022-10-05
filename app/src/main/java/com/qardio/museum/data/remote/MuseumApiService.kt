package com.qardio.museum.data.remote

import com.qardio.museum.model.ArtResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Configuration to fetch arts via Retrofit.
 */
interface MuseumApiService {

    @GET("collection/")
    suspend fun getArtsByCreator(
        @Query("involvedMaker") maker: String? = null,
        @Query("p") page: Any = 1,
        @Query("ps") pageSize: Int? = 1,
    ) : ArtResponse

}