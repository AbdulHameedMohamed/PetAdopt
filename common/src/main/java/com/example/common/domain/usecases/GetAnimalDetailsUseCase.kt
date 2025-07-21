package com.example.common.domain.usecases

import com.example.common.domain.model.animal.Animal
import com.example.common.domain.repository.AnimalRepository
import io.reactivex.Single
import javax.inject.Inject

class GetAnimalDetailsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(
        animalId: Long
    ): Single<Animal> {
        return animalRepository.getAnimal(animalId)
    }
}