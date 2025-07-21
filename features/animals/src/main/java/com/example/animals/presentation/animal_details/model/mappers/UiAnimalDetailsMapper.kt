package com.example.animals.presentation.animal_details.model.mappers

import com.example.animals.presentation.animal_details.model.UIAnimalDetailed
import com.example.common.domain.model.animal.Animal
import com.example.common.presentation.model.mappers.UiMapper
import javax.inject.Inject

class UiAnimalDetailsMapper @Inject constructor() : UiMapper<Animal, UIAnimalDetailed> {

    override fun mapToView(input: Animal): UIAnimalDetailed {
        val details = input.details
            ?: throw IllegalStateException("Animal details must not be null for animal with id=${input.id}")

        return UIAnimalDetailed(
            id = input.id,
            name = input.name,
            photo = input.media.getFirstSmallestAvailablePhoto(),
            description = details.description,
            sprayNeutered = details.healthDetails.isSpayedOrNeutered,
            specialNeeds = details.healthDetails.hasSpecialNeeds,
            declawed = details.healthDetails.isDeclawed,
            shotsCurrent = details.healthDetails.shotsAreCurrent,
            tags = input.tags,
            phone = details.organization.contact.phone
        )
    }
}