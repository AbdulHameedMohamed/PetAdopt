package com.example.petadopt.animals.domain.repository

import com.example.petadopt.animals.domain.model.animal.Animal
import com.example.petadopt.animals.domain.model.pagination.PaginatedAnimals
import io.reactivex.Flowable

interface AnimalRepository {
    fun getAnimals(): Flowable<List<Animal>>

    suspend fun requestMoreAnimals(
        pageToLoad: Int, numberOfItems: Int
    ): PaginatedAnimals

    suspend fun storeAnimals(animals: List<Animal>)
}