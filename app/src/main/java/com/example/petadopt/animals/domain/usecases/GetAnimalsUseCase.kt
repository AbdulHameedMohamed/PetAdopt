package com.example.petadopt.common.domain.usecases

import com.example.petadopt.common.domain.repository.AnimalRepository
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    operator fun invoke() = animalRepository.getAnimals().filter { it.isNotEmpty() }
}