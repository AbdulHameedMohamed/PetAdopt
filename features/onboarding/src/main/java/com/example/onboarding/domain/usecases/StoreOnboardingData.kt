package com.example.onboarding.domain.usecases

import com.example.common.domain.repository.AnimalRepository
import com.example.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoreOnboardingData @Inject constructor(
    private val repository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {

  suspend operator fun invoke(postcode: String, distance: String) {
    withContext(dispatchersProvider.io()) {
      repository.storeOnboardingData(postcode, distance.toInt())
    }
  }
}