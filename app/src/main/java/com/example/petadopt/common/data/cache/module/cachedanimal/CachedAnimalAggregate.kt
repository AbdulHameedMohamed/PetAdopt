package com.example.petadopt.common.data.cache.module.cachedanimal

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.petadopt.common.domain.model.animal.Animal

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
) {

    companion object {
        fun fromDomain(animalWithDetails: Animal): CachedAnimalAggregate {
            return CachedAnimalAggregate(
                animal = CachedAnimal.fromDomain(animalWithDetails),
                photos = animalWithDetails.media.photos.map {
                    CachedPhoto.fromDomain(animalWithDetails.id, it)
                },
                videos = animalWithDetails.media.videos.map {
                    CachedVideo.fromDomain(animalWithDetails.id, it)
                },
                tags = animalWithDetails.tags.map { CachedTag(it) }
            )
        }
    }
}
