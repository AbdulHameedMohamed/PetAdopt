package com.example.common.data.di

import android.content.Context
import androidx.room.Room
import com.example.common.data.cache.Cache
import com.example.common.data.cache.PetAdoptDatabase
import com.example.common.data.cache.RoomCache
import com.example.common.data.cache.daos.AnimalsDao
import com.example.common.data.cache.daos.OrganizationsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): PetAdoptDatabase {
            return Room.databaseBuilder(
                context,
                PetAdoptDatabase::class.java,
                "petadopt.db"
            ).build()
        }

        @Provides
        fun provideAnimalsDao(
            petAdoptDatabase: PetAdoptDatabase
        ): AnimalsDao = petAdoptDatabase.animalsDao()

        @Provides
        fun provideOrganizationsDao(petAdoptDatabase: PetAdoptDatabase): OrganizationsDao =
            petAdoptDatabase.organizationsDao()
    }
}