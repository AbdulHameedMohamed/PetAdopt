package com.example.petadopt.animals.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedAnimalTagCrossRef
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedAnimal
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedPhoto
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedTag
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedVideo
import com.example.petadopt.animals.data.daos.AnimalsDao
import com.example.petadopt.animals.data.daos.OrganizationsDao

@Database(
    entities = [
        CachedPhoto::class,
        CachedVideo::class,
        CachedTag::class,
        CachedAnimal::class,
        CachedAnimalTagCrossRef::class
    ], version = 1, exportSchema = false
)
abstract class PetSaveDatabase : RoomDatabase() {
    abstract fun organizationsDao(): OrganizationsDao
    abstract fun animalsDao(): AnimalsDao
}