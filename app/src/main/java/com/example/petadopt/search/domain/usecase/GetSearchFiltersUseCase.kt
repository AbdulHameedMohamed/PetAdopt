package com.example.petadopt.search.domain.usecase

import com.example.common.domain.model.animal.details.Age
import com.example.common.domain.model.search.SearchFilters
import com.example.common.domain.repository.AnimalRepository
import com.example.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class GetSearchFiltersUseCase @Inject constructor(
    private val repository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    companion object {
        const val NO_FILTER_SELECTED = "Any"
    }

    suspend operator fun invoke(): SearchFilters {
        return withContext(dispatchersProvider.io()) {
            val unknown = Age.UNKNOWN.name

            val types =
                listOf(NO_FILTER_SELECTED) +
                        repository.getAnimalTypes()

            val ages = repository.getAnimalAges()
                .map { age ->

                    if (age.name == unknown) {
                        NO_FILTER_SELECTED
                    } else {
                        age.name
                            .uppercase()
                            .replaceFirstChar { firstChar ->
                                if (firstChar.isLowerCase()) {
                                    firstChar.titlecase(Locale.ROOT)
                                } else {
                                    firstChar.toString()
                                }
                            }
                    }
                }
            return@withContext SearchFilters(ages, types)
        }
    }
}