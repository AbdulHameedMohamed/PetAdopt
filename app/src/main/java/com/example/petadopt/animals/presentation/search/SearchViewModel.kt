package com.example.petadopt.animals.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petadopt.animals.domain.NoMoreAnimalsException
import com.example.petadopt.animals.domain.usecases.GetSearchFiltersUseCase
import com.example.petadopt.animals.utils.createExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getFilterValues: GetSearchFiltersUseCase,
    private val compositeDisposable: CompositeDisposable
): ViewModel() {

    private val _state = MutableLiveData<SearchViewState>()
    val state: LiveData<SearchViewState> get() = _state

    init {
        _state.value = SearchViewState()
    }

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.PrepareForSearch -> prepareForSearch()
            else -> {
                // TODO
            }
        }
    }

    private fun prepareForSearch() {
        loadFilterValues()
    }

    private fun loadFilterValues() {
        val exceptionHandler = createExceptionHandler("Error while getting filter values")

        viewModelScope.launch(exceptionHandler) {
            Log.d("abdulhameed", "loadFilterValues: Starting The Request")
            val (ages, types) = getFilterValues()
            Log.d("abdulhameed", "loadFilterValues: $ages")
            Log.d("abdulhameed", "loadFilterValues: $types")
            updateStateWithFilterValues(ages, types)
        }
    }

    private fun updateStateWithFilterValues(ages: List<String>, types: List<String>) {
        _state.value = state.value!!.updateToReadyToSearch(
            ages, types
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
