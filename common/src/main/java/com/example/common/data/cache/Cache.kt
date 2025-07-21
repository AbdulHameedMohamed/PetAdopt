package com.example.common.data.cache

import com.example.common.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.common.data.cache.module.cachedorganization.CachedOrganization
import io.reactivex.Flowable
import io.reactivex.Single

interface Cache {
    suspend fun storeOrganizations(organizations: List<CachedOrganization>)
    fun getOrganization(organizationId: String): Single<CachedOrganization>

    fun getNearbyAnimals(): Flowable<List<CachedAnimalAggregate>>

    suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>)

    fun getAnimal(animalId: Long): Single<CachedAnimalAggregate>

    suspend fun getAllTypes(): List<String>

    fun searchAnimalsBy(
        name: String,
        age: String,
        type: String
    ): Flowable<List<CachedAnimalAggregate>>
}