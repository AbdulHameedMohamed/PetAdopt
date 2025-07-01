package com.example.petadopt.animals.domain.usecases

import com.example.petadopt.common.domain.model.NoMoreAnimalsException
import com.example.petadopt.common.domain.model.pagination.Pagination
import com.example.petadopt.common.domain.repository.AnimalRepository
import com.example.petadopt.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int,
        pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
    ): Pagination = withContext(dispatchersProvider.io()) {
        val (animals, pagination) =
            animalRepository.requestMoreAnimals(
                pageToLoad,
                pageSize
            )
        if (animals.isEmpty()) {
            throw NoMoreAnimalsException("No animals nearby :(")
        }
        animalRepository.storeAnimals(animals)
        return@withContext pagination
    }
}