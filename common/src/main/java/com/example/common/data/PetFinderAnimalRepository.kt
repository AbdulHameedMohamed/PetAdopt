package com.example.common.data

import android.util.Log
import com.example.common.data.api.PetFinderApi
import com.example.common.data.api.model.mapper.ApiAnimalMapper
import com.example.common.data.api.model.mapper.ApiPaginationMapper
import com.example.common.data.cache.Cache
import com.example.common.domain.model.animal.Animal
import com.example.common.domain.model.pagination.PaginatedAnimals
import com.example.common.domain.repository.AnimalRepository
import com.example.common.data.cache.module.cachedanimal.CachedAnimalAggregate
import com.example.common.data.cache.module.cachedorganization.CachedOrganization
import com.example.common.data.preferences.Preferences
import com.example.common.domain.model.NetworkException
import com.example.common.domain.model.search.SearchParameters
import com.example.common.domain.model.search.SearchResults
import com.example.common.domain.model.animal.details.Age
import io.reactivex.Flowable
import retrofit2.HttpException
import javax.inject.Inject

class PetFinderAnimalRepository @Inject constructor(
    private val api: PetFinderApi,
    private val cache: Cache,
    private val preferences: Preferences,
    private val apiAnimalMapper: ApiAnimalMapper,
    private val apiPaginationMapper: ApiPaginationMapper
) : AnimalRepository {

    override fun getAnimals(): Flowable<List<Animal>> {
        return cache.getNearbyAnimals()
            .distinctUntilChanged()
            .map { animalList ->
                Log.d("hitler", "getAnimals: ${animalList.map { it.animal.toAnimalDomain(it.photos, it.videos, it.tags) }}")

                animalList.map { it.animal.toAnimalDomain(it.photos, it.videos, it.tags) }
            }
    }

    override suspend fun requestMoreAnimals(pageToLoad: Int, numberOfItems: Int): PaginatedAnimals {
        val postcode = preferences.getPostcode()
        val maxDistanceMiles = preferences.getMaxDistanceAllowedToGetAnimals()

        try {
            val (apiAnimals, apiPagination) = api.getNearbyAnimals(
                pageToLoad,
                numberOfItems,
                postcode,
                maxDistanceMiles
            )

            Log.d("abdulhameed", "requestMoreAnimals: $apiAnimals")
            return PaginatedAnimals(
                apiAnimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(),
                apiPaginationMapper.mapToDomain(apiPagination)
            )
        } catch (exception: HttpException) {
            exception.printStackTrace()
            Log.d("abdulhameed", "requestMoreAnimals: ${exception.message() ?: exception.code()}")
            throw NetworkException(exception.message ?: "Code ${exception.code()}")
        }
    }

    override suspend fun storeAnimals(animals: List<Animal>) {
        val organizations = animals.map {
            requireNotNull(it.details) { "Details must be present when storing animals" }.organization
        }.map(CachedOrganization::fromDomain)

        cache.storeOrganizations(organizations)
        cache.storeNearbyAnimals(animals.map { CachedAnimalAggregate.fromDomain(it) })
    }

    override suspend fun getAnimal(animalId: Long): Animal {
        val (animal, photos, videos, tags) = cache.getAnimal(animalId)
        val organization = cache.getOrganization(animal.organizationId)

        return animal.toDomain(photos, videos, tags, organization)
    }


    override suspend fun getAnimalTypes(): List<String> {
        return cache.getAllTypes()
    }

    override fun getAnimalAges(): List<Age> {
        return Age.entries
    }

    override fun searchCachedAnimalsBy(searchParameters: SearchParameters): Flowable<SearchResults> {
        val (name, age, type) = searchParameters

        return cache.searchAnimalsBy(name, age, type)
            .distinctUntilChanged().map { animalList ->
                animalList.map { it.animal.toAnimalDomain(it.photos, it.videos, it.tags) }
            }
            .map { SearchResults(it, searchParameters) }
    }

    override suspend fun searchAnimalsRemotely(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        pageSize: Int
    ): PaginatedAnimals {

        val postcode = preferences.getPostcode()
        val maxDistanceMiles = preferences.getMaxDistanceAllowedToGetAnimals()

        val (apiAnimals, apiPagination) = api.searchAnimalsBy(
            searchParameters.name,
            searchParameters.age,
            searchParameters.type,
            pageToLoad,
            pageSize,
            postcode,
            maxDistanceMiles
        )

        return PaginatedAnimals(
            apiAnimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(),
            apiPaginationMapper.mapToDomain(apiPagination)
        )
    }

    override suspend fun storeOnboardingData(postcode: String, distance: Int) {
        with(preferences) {
            putPostcode(postcode)
            putMaxDistanceAllowedToGetAnimals(distance)
        }
    }

    override suspend fun onboardingIsComplete(): Boolean {
        return preferences.getPostcode().isNotEmpty() &&
                preferences.getMaxDistanceAllowedToGetAnimals() > 0
    }
}