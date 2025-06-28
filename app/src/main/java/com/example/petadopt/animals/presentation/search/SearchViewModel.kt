package com.example.petadopt.animals.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petadopt.animals.domain.NoMoreAnimalsException
import com.example.petadopt.animals.domain.model.animal.Animal
import com.example.petadopt.animals.domain.model.animal.SearchResults
import com.example.petadopt.animals.domain.usecases.GetSearchFiltersUseCase
import com.example.petadopt.animals.domain.usecases.SearchAnimalsUseCase
import com.example.petadopt.animals.presentation.model.mappers.UiAnimalMapper
import com.example.petadopt.animals.utils.createExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchAnimalsUseCase: SearchAnimalsUseCase,
    private val getFilterValues: GetSearchFiltersUseCase,
    private val uiAnimalMapper: UiAnimalMapper,
    private val compositeDisposable: CompositeDisposable
): ViewModel() {

    private val _state = MutableLiveData<SearchViewState>()
    val state: LiveData<SearchViewState> get() = _state

    private val querySubject = BehaviorSubject.create<String>()
    private val ageSubject = BehaviorSubject.createDefault("")
    private val typeSubject = BehaviorSubject.createDefault("")

    var currentPage = 0

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
        when(event) {
            is SearchEvent.QueryInput -> updateQuery(event.input)
            is SearchEvent.AgeValueSelected -> updateAgeValue(event.age)
            is SearchEvent.TypeValueSelected -> updateTypeValue(event.type)
            else -> {}
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
            )
            .addTo(compositeDisposable)
    }

    private fun updateStateWithFilterValues(ages: List<String>, types: List<String>) {
        _state.value = state.value!!.updateToReadyToSearch(
            ages, types
        )
    }

    private fun onSearchResults(searchResults: SearchResults) {
        val (animals, searchParameters) = searchResults
        if (animals.isEmpty()) {
            // search remotely
        } else {
            onAnimalList(animals)
        }
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
