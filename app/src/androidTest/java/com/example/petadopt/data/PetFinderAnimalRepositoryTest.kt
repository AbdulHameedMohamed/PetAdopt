package com.example.petadopt.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.petadopt.animals.data.PetFinderAnimalRepository
import com.example.petadopt.animals.data.PetFinderApi
import com.example.petadopt.animals.data.api.model.mapper.ApiAnimalMapper
import com.example.petadopt.animals.data.api.model.mapper.ApiPaginationMapper
import com.example.petadopt.animals.data.cache.Cache
import com.example.petadopt.animals.data.cache.PetSaveDatabase
import com.example.petadopt.animals.data.di.PreferencesModule
import com.example.petadopt.animals.data.preferences.Preferences
import com.example.petadopt.animals.domain.repository.AnimalRepository
import com.example.petadopt.data.api.utils.FakeServer
import com.example.petadopt.data.preferences.FakePreferences
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import java.time.Instant
import javax.inject.Inject
import com.google.common.truth.Truth.assertThat

@HiltAndroidTest
@UninstallModules(PreferencesModule::class)
class PetFinderAnimalRepositoryTest {

    private val fakeServer = FakeServer()
    private lateinit var repository: AnimalRepository
    private lateinit var api: PetFinderApi
    @Inject
    lateinit var cache: Cache

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: PetSaveDatabase

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Inject
    lateinit var apiAnimalMapper: ApiAnimalMapper

    @Inject
    lateinit var apiPaginationMapper: ApiPaginationMapper

    @BindValue
    val preferences: Preferences = FakePreferences()

    @Before
    fun setup() {
        fakeServer.start()

        preferences.deleteTokenInfo()
        preferences.putToken("validToken")
        preferences.putTokenExpirationTime(
            Instant.now().plusSeconds(3600).epochSecond
        )
        preferences.putTokenType("Bearer")
        hiltRule.inject()

        api = retrofitBuilder
            .baseUrl(fakeServer.baseEndpoint)
            .build()
            .create(PetFinderApi::class.java)

        repository = PetFinderAnimalRepository(
            api,
            cache,
            apiAnimalMapper,
            apiPaginationMapper
        )
    }

    @Test
    fun requestMoreAnimals_success() = runBlocking {
        // Given
        val expectedAnimalId = 124L
        fakeServer.setHappyPathDispatcher()

        // When
        val paginatedAnimals = repository.requestMoreAnimals(1, 100)

        // Then
        val animal = paginatedAnimals.animals.first()
        assertThat(animal.id).isEqualTo(expectedAnimalId)
    }

    @After
    fun teardown() {
        fakeServer.shutdown()
    }
}