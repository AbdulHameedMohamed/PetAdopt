package com.example.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.common.data.cache.module.cachedanimal.CachedAnimal
import com.example.common.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.common.data.cache.module.cachedanimal.CachedPhoto
import com.example.common.data.cache.module.cachedanimal.CachedTag
import com.example.common.data.cache.module.cachedanimal.CachedVideo
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class AnimalsDao {

    @Transaction
    @Query("SELECT * FROM animals ORDER BY animalId DESC")
    abstract fun getAllAnimals(): Flowable<List<CachedAnimalAggregate>>

    @Transaction
    @Query("SELECT * FROM animals WHERE animalId IS :animalId")
    abstract fun getAnimal(
        animalId: Long
    ): Single<CachedAnimalAggregate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAnimalAggregate(
        animal: CachedAnimal,
        photos: List<CachedPhoto>,
        videos: List<CachedVideo>,
        tags: List<CachedTag>
    )

    suspend fun insertAnimals(animalAggregates: List<CachedAnimalAggregate>) {
        for (animalAggregate in animalAggregates) {
            insertAnimalAggregate(
                animalAggregate.animal,
                animalAggregate.photos,
                animalAggregate.videos,
                animalAggregate.tags
            )
        }
    }

    @Query("SELECT DISTINCT type FROM animals")
    abstract suspend fun getAllTypes(): List<String>

    @Transaction
    @Query(
        """
        SELECT * FROM animals
        WHERE name LIKE '%' || :name || '%'
        AND age LIKE '%' || :age || '%'
        AND type LIKE '%' || :type || '%'
    """
    )
    abstract fun searchAnimalsBy(
        name: String,
        age: String,
        type: String
    ): Flowable<List<CachedAnimalAggregate>>
}