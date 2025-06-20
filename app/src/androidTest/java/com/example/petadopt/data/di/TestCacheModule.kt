package com.example.petadopt.data.di

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.petadopt.animals.data.cache.PetAdoptDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TestCacheModule {
    @Provides
    fun provideRoomDatabase(): PetAdoptDatabase {
        return Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PetAdoptDatabase::class.java
        ).allowMainThreadQueries().build()
    }
}