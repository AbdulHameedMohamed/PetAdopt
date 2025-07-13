package com.example.petadopt.main.domain.usecases

import com.example.common.domain.repository.AnimalRepository
import com.example.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnboardingIsCompleteUseCase @Inject constructor(
    private val repository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke() = withContext(dispatchersProvider.io()) {
        repository.onboardingIsComplete()
    }
}