package com.example.petadopt.animals.data.cache.module.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petadopt.animals.data.cache.module.cachedorganization.CachedOrganization
import com.example.petadopt.animals.domain.model.animal.AdoptionStatus
import com.example.petadopt.animals.domain.model.animal.Animal
import com.example.petadopt.animals.domain.model.animal.Media
import com.example.petadopt.animals.domain.model.animal.details.Age
import com.example.petadopt.animals.domain.model.animal.details.Breed
import com.example.petadopt.animals.domain.model.animal.details.Coat
import com.example.petadopt.animals.domain.model.animal.details.Colors
import com.example.petadopt.animals.domain.model.animal.details.Details
import com.example.petadopt.animals.domain.model.animal.details.Gender
import com.example.petadopt.animals.domain.model.animal.details.HabitatAdaptation
import com.example.petadopt.animals.domain.model.animal.details.HealthDetails
import com.example.petadopt.animals.domain.model.animal.details.Size
import com.example.petadopt.utils.DateTimeUtils

@Entity(
    tableName = "animals"
)
data class CachedAnimal(
    @PrimaryKey
    val animalId: Long,
    val organizationId: String,
    val name: String,
    val type: String,
    val description: String,
    val age: String,
    val species: String,
    val primaryBreed: String,
    val secondaryBreed: String,
    val primaryColor: String,
    val secondaryColor: String,
    val tertiaryColor: String,
    val gender: String,
    val size: String,
    val coat: String,
    val isSpayedOrNeutered: Boolean,
    val isDeclawed: Boolean,
    val hasSpecialNeeds: Boolean,
    val shotsAreCurrent: Boolean,
    val goodWithChildren: Boolean,
    val goodWithDogs: Boolean,
    val goodWithCats: Boolean,
    val adoptionStatus: String,
    val publishedAt: String
) {
    companion object {
        fun fromDomain(domainModel: Animal): CachedAnimal {
            requireNotNull(domainModel.details) { "Details must not be null when mapping to CachedAnimal" }
            val details = domainModel.details
            val healthDetails = details.healthDetails
            val habitatAdaptation = details.habitatAdaptation

            return CachedAnimal(
                animalId = domainModel.id,
                organizationId = details.organization.id,
                name = domainModel.name,
                type = domainModel.type,
                description = details.description,
                age = details.age.toString(),
                species = details.species,
                primaryBreed = details.breed.primary,
                secondaryBreed = details.breed.secondary,
                primaryColor = details.colors.primary,
                secondaryColor = details.colors.secondary,
                tertiaryColor = details.colors.tertiary,
                gender = details.gender.toString(),
                size = details.size.toString(),
                coat = details.coat.toString(),
                isSpayedOrNeutered = healthDetails.isSpayedOrNeutered,
                isDeclawed = healthDetails.isDeclawed,
                hasSpecialNeeds = healthDetails.hasSpecialNeeds,
                shotsAreCurrent = healthDetails.shotsAreCurrent,
                goodWithChildren = habitatAdaptation.goodWithChildren,
                goodWithDogs = habitatAdaptation.goodWithDogs,
                goodWithCats = habitatAdaptation.goodWithCats,
                adoptionStatus = domainModel.adoptionStatus.toString(),
                publishedAt = domainModel.publishedAt.toString()
            )
        }
    }

    fun toDomain(
        photos: List<CachedPhoto>,
        videos: List<CachedVideo>,
        tags: List<CachedTag>,
        organization: CachedOrganization
    ): Animal {
        return Animal(
            id = animalId,
            name = name,
            type = type,
            details = mapDetails(organization),
            media = Media(
                photos = photos.map { it.toDomain() },
                videos = videos.map { it.toDomain() }
            ),
            tags = tags.map { it.tag },
            adoptionStatus = AdoptionStatus.valueOf(adoptionStatus),
            publishedAt = DateTimeUtils.parse(publishedAt)
        )
    }

    fun toAnimalDomain(
        photos: List<CachedPhoto>,
        videos: List<CachedVideo>,
        tags: List<CachedTag>
    ): Animal {
        return Animal(
            id = animalId,
            name = name,
            type = type,
            details = null,
            media = Media(
                photos = photos.map { it.toDomain() },
                videos = videos.map { it.toDomain() }
            ),
            tags = tags.map { it.tag },
            adoptionStatus = AdoptionStatus.valueOf(adoptionStatus),
            publishedAt = DateTimeUtils.parse(publishedAt)
        )
    }

    private fun mapDetails(organization: CachedOrganization): Details {
        return Details(
            description = description,
            age = Age.valueOf(age),
            species = species,
            breed = Breed(primaryBreed, secondaryBreed),
            colors = Colors(primaryColor, secondaryColor, tertiaryColor),
            gender = Gender.valueOf(gender),
            size = Size.valueOf(size),
            coat = Coat.valueOf(coat),
            healthDetails = HealthDetails(
                isSpayedOrNeutered, isDeclawed,
                hasSpecialNeeds, shotsAreCurrent
            ),
            habitatAdaptation = HabitatAdaptation(
                goodWithChildren, goodWithDogs,
                goodWithCats
            ),
            organization = organization.toDomain()
        )
    }
}