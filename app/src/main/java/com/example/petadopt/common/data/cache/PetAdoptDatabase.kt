package com.example.petadopt.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petadopt.common.data.cache.module.cachedanimal.CachedAnimalTagCrossRef
import com.example.petadopt.common.data.cache.module.cachedanimal.CachedAnimal
import com.example.petadopt.common.data.cache.module.cachedanimal.CachedPhoto
import com.example.petadopt.common.data.cache.module.cachedanimal.CachedTag
import com.example.petadopt.common.data.cache.module.cachedanimal.CachedVideo
import com.example.petadopt.common.data.cache.module.cachedorganization.CachedOrganization
import com.example.petadopt.common.data.cache.daos.AnimalsDao
import com.example.petadopt.common.data.cache.daos.OrganizationsDao

@Database(
    entities = [
        CachedPhoto::class,
        CachedVideo::class,
        CachedTag::class,
        CachedAnimal::class,
        CachedAnimalTagCrossRef::class,
        CachedOrganization::class
    ], version = 1, exportSchema = false
)
abstract class PetAdoptDatabase : RoomDatabase() {
    abstract fun organizationsDao(): OrganizationsDao
    abstract fun animalsDao(): AnimalsDao
}