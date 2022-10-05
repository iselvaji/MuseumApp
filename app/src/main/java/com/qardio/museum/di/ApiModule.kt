package com.qardio.museum.di

import com.qardio.museum.BuildConfig
import com.qardio.museum.data.remote.MuseumApiService
import com.qardio.museum.utils.Constants
import com.qardio.museum.utils.Constants.Companion.REQUEST_TIME_OUT
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * API module used Hilt dependency injection for Retrofit api related classes
 * It provides the instance of HttpClient, Interceptors, Converter factories and api services
 */

@Module
@InstallIn(SingletonComponent::class)
object ApiModule{

    @Singleton
    @Provides
    fun provideHttpClient(interceptor: Interceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(loggingInterceptor)
            .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIME_OUT,TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideApiKeyInterceptor() : Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val httpUrl = original.url
            val url = httpUrl.newBuilder()
                .addQueryParameter(Constants.API_KEY, BuildConfig.API_KEY)
                .build()
            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MuseumApiService =
        retrofit.create(MuseumApiService::class.java)
}