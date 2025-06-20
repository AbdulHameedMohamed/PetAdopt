package com.example.petadopt.animals.data.di

import com.example.petadopt.animals.data.preferences.PetAdoptPreferences
import com.example.petadopt.animals.data.preferences.Preferences
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