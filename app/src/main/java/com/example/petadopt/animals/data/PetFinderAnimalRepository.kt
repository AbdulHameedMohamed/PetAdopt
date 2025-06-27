package com.example.petadopt.animals.data

import com.example.petadopt.animals.data.cache.Cache
import com.example.petadopt.animals.data.api.model.mapper.ApiAnimalMapper
import com.example.petadopt.animals.data.api.model.mapper.ApiPaginationMapper
import com.example.petadopt.animals.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.petadopt.animals.data.cache.module.cachedorganization.CachedOrganization
import com.example.petadopt.animals.domain.model.animal.Animal
import com.example.petadopt.animals.domain.model.animal.details.Age
import com.example.petadopt.animals.domain.model.pagination.PaginatedAnimals
import com.example.petadopt.animals.domain.repository.AnimalRepository
import io.reactivex.Flowable
import javax.inject.Inject

class PetFinderAnimalRepository @Inject constructor(
    private val api: PetFinderApi,
    private val cache: Cache,
    private val apiAnimalMapper: ApiAnimalMapper,
    private val apiPaginationMapper: ApiPaginationMapper
) : AnimalRepository {
    override fun getAnimals(): Flowable<List<Animal>> {
        return cache.getNearbyAnimals()
            .distinctUntilChanged()
            .map { animalList ->
                animalList.map {
                    it.animal.toAnimalDomain(
                        it.photos,
                        it.videos,
                        it.tags
                    )
                }
            }
    }

    override suspend fun requestMoreAnimals(pageToLoad: Int, numberOfItems: Int): PaginatedAnimals {
        val (apiAnimals, apiPagination) = api.getNearbyAnimals(
            pageToLoad,
            numberOfItems,
            postcode,
            maxDistanceMiles
        )
        return PaginatedAnimals(
            apiAnimals?.map {
                apiAnimalMapper.mapToDomain(it)
            }.orEmpty(),
            apiPaginationMapper.mapToDomain(apiPagination)
        )
    }

    override suspend fun storeAnimals(animals: List<Animal>) {
        val organizations = animals.map {
            requireNotNull(it.details) { "Details must be present when storing animals" }.organization
        }.map(CachedOrganization::fromDomain)

        cache.storeOrganizations(organizations)
        cache.storeNearbyAnimals(animals.map { CachedAnimalAggregate.fromDomain(it) })
    }

    override suspend fun getAnimalTypes(): List<String> {
        return cache.getAllTypes()
    }

    override fun getAnimalAges(): List<Age> {
        return Age.values().toList()
    }

    companion object {
        private const val postcode = "07097"
        private const val maxDistanceMiles = 100
    }
}