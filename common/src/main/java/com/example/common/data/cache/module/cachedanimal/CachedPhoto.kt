package com.example.common.data.cache.module.cachedanimal

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.common.domain.model.animal.Media

@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = CachedAnimal::class,
            parentColumns = ["animalId"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("animalId")]
)
data class CachedPhoto(
    @PrimaryKey(autoGenerate = true)
    val photoId: Long = 0,
    val animalId: Long,
    val medium: String,
    val full: String
) {
    companion object {
        fun fromDomain(animalId: Long, photo: com.example.common.domain.model.animal.Media.Photo): CachedPhoto {
            val (medium, full) = photo

            return CachedPhoto(animalId = animalId, medium = medium, full = full)
        }
    }

    fun toDomain(): com.example.common.domain.model.animal.Media.Photo = com.example.common.domain.model.animal.Media.Photo(medium, full)
}