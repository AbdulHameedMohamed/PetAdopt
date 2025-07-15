package com.example.common.domain.usecases

import com.example.common.domain.model.animal.Animal
import com.example.common.domain.repository.AnimalRepository
import com.example.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAnimalDetailsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {

  suspend operator fun invoke(animalId: Long): Animal {
    return withContext(dispatchersProvider.io()) {
        animalRepository.getAnimal(animalId)
    }
  }
}