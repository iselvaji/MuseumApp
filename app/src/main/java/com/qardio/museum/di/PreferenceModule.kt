package com.qardio.museum.di

import android.content.Context
import android.content.SharedPreferences
import com.qardio.museum.utils.Constants.Companion.PREFERENCE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module used Hilt dependency injection for Shared preference
 * It provides the instance of Shared preference
 */


@InstallIn(SingletonComponent::class)
@Module
class PreferenceModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
}