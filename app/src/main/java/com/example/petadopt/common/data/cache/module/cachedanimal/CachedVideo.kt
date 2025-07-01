package com.example.petadopt.common.data.cache.module.cachedanimal

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.petadopt.common.domain.model.animal.Media

@Entity(
    tableName = "videos",
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
data class CachedVideo(
    @PrimaryKey(autoGenerate = true)
    val videoId: Long = 0,
    val animalId: Long,
    val video: String
) {
    companion object {
        fun fromDomain(animalId: Long, video: com.example.petadopt.common.domain.model.animal.Media.Video): CachedVideo {
            return CachedVideo(animalId = animalId, video = video.video)
        }
    }

    fun toDomain(): com.example.petadopt.common.domain.model.animal.Media.Video = com.example.petadopt.common.domain.model.animal.Media.Video(video)
}
