package com.example.petadopt.animals.domain.usecases

import com.example.petadopt.animals.domain.NoMoreAnimalsException
import com.example.petadopt.animals.domain.model.animal.SearchParameters
import com.example.petadopt.animals.domain.model.pagination.Pagination
import com.example.petadopt.animals.domain.repository.AnimalRepository
import javax.inject.Inject

class SearchAnimalsRemotelyUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    suspend operator fun invoke(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
    ): Pagination {
        val (animals, pagination) =
            repository.searchAnimalsRemotely(pageToLoad, searchParameters, pageSize)

        if (animals.isEmpty()) {
            throw NoMoreAnimalsException("Couldn't find more animals that match the search parameters.")
        }

        repository.storeAnimals(animals)

        return pagination
    }
}