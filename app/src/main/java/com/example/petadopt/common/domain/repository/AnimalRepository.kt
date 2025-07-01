package com.example.petadopt.common.domain.repository

import com.example.petadopt.common.domain.model.animal.Animal
import com.example.petadopt.search.domain.model.SearchParameters
import com.example.petadopt.common.domain.model.animal.details.Age
import com.example.petadopt.common.domain.model.pagination.PaginatedAnimals
import com.example.petadopt.search.domain.model.SearchResults
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