package com.example.search.domain.usecase

import com.example.common.domain.model.NoMoreAnimalsException
import com.example.common.domain.model.pagination.Pagination
import com.example.common.domain.model.search.SearchParameters
import com.example.common.domain.repository.AnimalRepository
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