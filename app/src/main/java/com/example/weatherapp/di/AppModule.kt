package com.example.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun providesContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
    }

    @Singleton
    @Provides
    fun providesSharedPrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}