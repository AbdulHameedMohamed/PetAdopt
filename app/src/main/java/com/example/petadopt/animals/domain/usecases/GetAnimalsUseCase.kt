package com.example.petadopt.animals.domain.usecases

import com.example.common.domain.repository.AnimalRepository
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    operator fun invoke() = animalRepository.getAnimals().filter { it.isNotEmpty() }
}