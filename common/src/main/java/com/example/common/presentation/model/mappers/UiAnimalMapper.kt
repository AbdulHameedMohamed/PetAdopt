package com.example.common.presentation.model.mappers

import com.example.common.domain.model.animal.Animal
import com.example.common.presentation.model.UIAnimal
import javax.inject.Inject


class UiAnimalMapper @Inject constructor() : UiMapper<Animal, UIAnimal> {

    override fun mapToView(input: Animal): UIAnimal {
        return UIAnimal(
            id = input.id,
            name = input.name,
            photo = input.media.getFirstSmallestAvailablePhoto()
        )
    }
}
