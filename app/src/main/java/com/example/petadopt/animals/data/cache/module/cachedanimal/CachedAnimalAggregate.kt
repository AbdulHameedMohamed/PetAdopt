package com.example.petadopt.animals.data.cache.module.cachedanimal

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CachedAnimalAggregate(
    @Embedded
    val animal: CachedAnimal,
    @Relation(
        parentColumn = "animalId",
        entityColumn = "animalId"
    )
    val photos: List<CachedPhoto>,
    @Relation(
        parentColumn = "animalId",
        entityColumn = "animalId"
    )
    val videos: List<CachedVideo>,
    @Relation(
        parentColumn = "animalId",
        entityColumn = "tag",
        associateBy = Junction(CachedAnimalTagCrossRef::class)
    )
    val tags: List<CachedTag>
)
