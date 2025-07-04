package com.example.common.data.di

import com.example.common.data.preferences.PetAdoptPreferences
import com.example.common.data.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun providePreferences(preferences: PetAdoptPreferences): Preferences
}