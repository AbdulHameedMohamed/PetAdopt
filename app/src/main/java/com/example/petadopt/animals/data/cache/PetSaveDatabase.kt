package com.example.petadopt.animals.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedAnimalTagCrossRef
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedAnimalWithDetails
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedPhoto
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedTag
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedVideo

@Database(
    entities = [
        CachedPhoto::class,
        CachedVideo::class,
        CachedTag::class,
        CachedAnimalWithDetails::class,
        CachedAnimalTagCrossRef::class
    ], version = 1, exportSchema = false
)
abstract class PetSaveDatabase : RoomDatabase()