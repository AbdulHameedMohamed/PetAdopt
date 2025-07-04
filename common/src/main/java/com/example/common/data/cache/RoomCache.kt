package com.example.common.data.cache

import com.example.common.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.common.data.cache.module.cachedorganization.CachedOrganization
import com.example.common.data.cache.daos.AnimalsDao
import com.example.common.data.cache.daos.OrganizationsDao
import io.reactivex.Flowable
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val animalsDao: AnimalsDao,
    private val organizationsDao: OrganizationsDao
) : Cache {

    override suspend fun storeOrganizations(organizations: List<CachedOrganization>) {
        organizationsDao.insert(organizations)
    }

    override fun getNearbyAnimals(): Flowable<List<CachedAnimalAggregate>> {
        return animalsDao.getAllAnimals()
    }

    override suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>) {
        animalsDao.insertAnimals(animals)
    }

    override suspend fun getAllTypes(): List<String> = animalsDao.getAllTypes()

    override fun searchAnimalsBy(
        name: String,
        age: String,
        type: String
    ): Flowable<List<CachedAnimalAggregate>> {
        return animalsDao.searchAnimalsBy(name, age, type)
    }
}