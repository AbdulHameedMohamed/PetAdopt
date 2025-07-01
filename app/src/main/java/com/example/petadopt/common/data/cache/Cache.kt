package com.example.petadopt.common.data.cache

import com.example.petadopt.common.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.petadopt.common.data.cache.module.cachedorganization.CachedOrganization
import io.reactivex.Flowable

interface Cache {
    suspend fun storeOrganizations(organizations: List<CachedOrganization>)

    fun getNearbyAnimals(): Flowable<List<CachedAnimalAggregate>>

    suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>)

    suspend fun getAllTypes(): List<String>

    fun searchAnimalsBy(
        name: String,
        age: String,
        type: String
    ): Flowable<List<CachedAnimalAggregate>>
}