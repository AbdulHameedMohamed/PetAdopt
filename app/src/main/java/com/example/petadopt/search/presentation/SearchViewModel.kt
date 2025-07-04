package com.example.petadopt.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.domain.model.NoMoreAnimalsException
import com.example.common.utils.DispatchersProvider
import com.example.common.utils.createExceptionHandler
import com.example.common.domain.model.animal.Animal
import com.example.common.domain.model.search.SearchParameters
import com.example.common.domain.model.pagination.Pagination
import com.example.petadopt.search.domain.usecase.GetSearchFiltersUseCase
import com.example.petadopt.search.domain.usecase.SearchAnimalsRemotelyUseCase
import com.example.petadopt.search.domain.usecase.SearchAnimalsUseCase
import com.example.common.presentation.model.mappers.UiAnimalMapper
import com.example.logging.domain.Logger
import com.example.common.domain.model.search.SearchResults
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchAnimalsUseCase: SearchAnimalsUseCase,
    private val getFilterValues: GetSearchFiltersUseCase,
    private val uiAnimalMapper: UiAnimalMapper,
    private val compositeDisposable: CompositeDisposable,
    private val dispatchersProvider: DispatchersProvider,
    private val searchAnimalsRemotelyUseCase: SearchAnimalsRemotelyUseCase
): ViewModel() {

    private val _state = MutableLiveData<SearchViewState>()
    val state: LiveData<SearchViewState> get() = _state

    private val querySubject = BehaviorSubject.create<String>()
    private val ageSubject = BehaviorSubject.createDefault("")
    private val typeSubject = BehaviorSubject.createDefault("")

    private var currentPage = 0

    private var remoteSearchJob: Job = Job()

    init {
        _state.value = SearchViewState()
    }

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.PrepareForSearch -> prepareForSearch()
            else -> {
                onSearchParametersUpdate(event)
            }
        }
    }

    private fun prepareForSearch() {
        loadFilterValues()
        setupSearchSubscription()
    }

    private fun onSearchParametersUpdate(event: SearchEvent) {
        remoteSearchJob.cancel(
            CancellationException("New search parameters incoming!")
        )
        when(event) {
            is SearchEvent.QueryInput -> updateQuery(event.input)
            is SearchEvent.AgeValueSelected -> updateAgeValue(event.age)
            is SearchEvent.TypeValueSelected -> updateTypeValue(event.type)
            else -> {
                Logger.d("Wrong SearchEvent in onSearchParametersUpdate!")
            }
        }
    }

    private fun updateQuery(input: String) {
        resetPagination()
        querySubject.onNext(input)

        if (input.isEmpty()) {
            setNoSearchQueryState()
        } else {
            setSearchingState()
        }
    }

    private fun resetPagination() {
        currentPage = 0
    }

    private fun setNoSearchQueryState() {
        _state.value = state.value!!.updateToNoSearchQuery()
    }

    private fun setSearchingState() {
        _state.value = state.value!!.updateToSearching()
    }

    private fun updateAgeValue(age: String) {
        ageSubject.onNext(age)
    }

    private fun updateTypeValue(type: String) {
        typeSubject.onNext(type)
    }

    private fun loadFilterValues() {
        val exceptionHandler = createExceptionHandler("Error while getting filter values")

        viewModelScope.launch(exceptionHandler) {
            val (ages, types) = getFilterValues()
            updateStateWithFilterValues(ages, types)
        }
    }

    private fun setupSearchSubscription() {
        searchAnimalsUseCase(querySubject, ageSubject, typeSubject)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSearchResults(it) },
                { onFailure(it) }
            ).addTo(compositeDisposable)
    }

    private fun updateStateWithFilterValues(ages: List<String>, types: List<String>) {
        _state.value = state.value!!.updateToReadyToSearch(
            ages, types
        )
    }

    private fun onSearchResults(searchResults: SearchResults) {
        val (animals, searchParameters) = searchResults
        if (animals.isEmpty()) {
            onEmptyCacheResults(searchParameters)
        } else {
            onAnimalList(animals)
        }
    }

    private fun onEmptyCacheResults(searchParameters: SearchParameters) {
        _state.value = state.value!!.updateToSearchingRemotely()
        searchRemotely(searchParameters)
    }

    private fun searchRemotely(searchParameters: SearchParameters) {
        val exceptionHandler = createExceptionHandler("Failed to search remotely.")

        remoteSearchJob = viewModelScope.launch(exceptionHandler) {
            val pagination = withContext(dispatchersProvider.io()) {
                searchAnimalsRemotelyUseCase(++currentPage, searchParameters)
            }

            onPaginationInfoObtained(pagination)
        }

        remoteSearchJob.invokeOnCompletion { it?.printStackTrace() }
    }

    private fun onPaginationInfoObtained(pagination: Pagination) {
        currentPage = pagination.currentPage
    }

    private fun onAnimalList(animals: List<Animal>) {
        _state.value = state.value!!.updateToHasSearchResults(
            animals.map { uiAnimalMapper.mapToView(it) }
        )
    }

    private fun createExceptionHandler(message: String): CoroutineExceptionHandler {
        return viewModelScope.createExceptionHandler(message) {
                onFailure(it)
            }
    }

    private fun onFailure(throwable: Throwable) {
        _state.value = if (throwable is NoMoreAnimalsException) {
            state.value!!.updateToNoResultsAvailable()
        } else {
            state.value!!.updateToHasFailure(throwable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
