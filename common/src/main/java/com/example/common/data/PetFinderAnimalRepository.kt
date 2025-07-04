package com.example.common.data

import com.example.common.data.api.PetFinderApi
import com.example.common.data.api.model.mapper.ApiAnimalMapper
import com.example.common.data.api.model.mapper.ApiPaginationMapper
import com.example.common.data.cache.Cache
import com.example.common.domain.model.animal.Animal
import com.example.common.domain.model.pagination.PaginatedAnimals
import com.example.common.domain.repository.AnimalRepository
import com.example.common.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.common.data.cache.module.cachedorganization.CachedOrganization
import com.example.common.domain.model.search.SearchParameters
import com.example.common.domain.model.search.SearchResults
import com.example.common.domain.model.animal.details.Age
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
            POST_CODE,
            MAX_DISTANCE_MILES
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
        return Age.entries
    }

    override fun searchCachedAnimalsBy(
        searchParameters: SearchParameters
    ): Flowable<SearchResults> {
        val (name, age, type) = searchParameters
        return cache.searchAnimalsBy(name, age, type)
            .distinctUntilChanged()
            .map { animalList ->
                animalList.map {
                    it.animal.toAnimalDomain(
                        it.photos,
                        it.videos,
                        it.tags
                    )
                }
            }.map { SearchResults(it, searchParameters) }
    }

    override suspend fun searchAnimalsRemotely(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        pageSize: Int
    ): PaginatedAnimals {
        val (apiAnimals, apiPagination) = api.searchAnimalsBy(
            pageToLoad,
            pageSize,
            POST_CODE,
            MAX_DISTANCE_MILES,
            searchParameters.name,
            searchParameters.age,
            searchParameters.type
        )

        val animals = apiAnimals?.map {
            apiAnimalMapper.mapToDomain(it)
        }.orEmpty()

        val pagination = apiPaginationMapper.mapToDomain(apiPagination)

        return PaginatedAnimals(
            animals, pagination
        )
    }

    companion object {
        private const val POST_CODE = "07097"
        private const val MAX_DISTANCE_MILES = 100
    }
}