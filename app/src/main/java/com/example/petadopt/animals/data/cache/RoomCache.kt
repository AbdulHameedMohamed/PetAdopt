package com.example.petadopt.animals.data.cache

import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.petadopt.animals.data.cache.module.cachedorganization.CachedOrganization
import com.example.petadopt.animals.data.cache.daos.AnimalsDao
import com.example.petadopt.animals.data.cache.daos.OrganizationsDao
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
}