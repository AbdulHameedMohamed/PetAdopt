package com.example.petadopt.animals.presentation.search
import com.example.petadopt.animals.presentation.utils.Event

data class SearchViewState(
    val ageFilterValues: Event<List<String>> = Event(emptyList()),
    val typeFilterValues: Event<List<String>> = Event(emptyList()),
    val searchingRemotely: Boolean = false,
    val noRemoteResults: Boolean = false,
    val failure: Event<Throwable>? = null
) {
    fun updateToReadyToSearch(ages: List<String>, types: List<String>): SearchViewState {
        return copy(
            ageFilterValues = Event(ages),
            typeFilterValues = Event(types)
        )
    }

    fun updateToNoResultsAvailable(): SearchViewState {
        return copy(searchingRemotely = false, noRemoteResults = true)
    }

    fun updateToHasFailure(throwable: Throwable): SearchViewState {
        return copy(failure = Event(throwable))
    }
}