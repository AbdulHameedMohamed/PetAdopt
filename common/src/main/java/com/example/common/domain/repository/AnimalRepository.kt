package com.example.common.domain.repository

import com.example.common.domain.model.animal.Animal
import com.example.common.domain.model.search.SearchParameters
import com.example.common.domain.model.animal.details.Age
import com.example.common.domain.model.pagination.PaginatedAnimals
import com.example.common.domain.model.search.SearchResults
import io.reactivex.Flowable

interface AnimalRepository {
    fun getAnimals(): Flowable<List<Animal>>

    suspend fun requestMoreAnimals(
        pageToLoad: Int, numberOfItems: Int
    ): PaginatedAnimals

    suspend fun storeAnimals(animals: List<Animal>)

    suspend fun getAnimalTypes(): List<String>

    fun getAnimalAges(): List<Age>

    fun searchCachedAnimalsBy(searchParameters: SearchParameters): Flowable<SearchResults>

    suspend fun searchAnimalsRemotely(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        pageSize: Int
    ): PaginatedAnimals
}