package com.qardio.museum

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.qardio.museum.data.remote.MuseumApiService
import com.qardio.museum.model.ArtResponse
import com.qardio.museum.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Test class which contains the test cases to validate API response fetched from remote
 *
 */

@RunWith(JUnit4::class)
class ArtApiTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockWebServer = MockWebServer()

    private lateinit var apiService: MuseumApiService

    @Before
    fun init() {

        val interceptor = Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val httpUrl = original.url
            val url = httpUrl.newBuilder()
                .addQueryParameter(Constants.API_KEY, BuildConfig.API_KEY)
                .build()
            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(BuildConfig.BASE_URL).toString())
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MuseumApiService::class.java)

    }

    @Test
    fun test_fetch_data() {

        val actual : ArtResponse = runBlocking { apiService.getArtsByCreator(Constants.CREATOR_NAME_TO_QUERY, 1, 5) }
        assertNotNull(actual)
        assert(actual.artObjects.isNotEmpty())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}