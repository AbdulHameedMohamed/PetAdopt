package com.example.petadopt.animals.presentation.animals_near_you

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petadopt.animals.domain.NetworkException
import com.example.petadopt.animals.domain.NetworkUnavailableException
import com.example.petadopt.animals.domain.NoMoreAnimalsException
import com.example.petadopt.animals.domain.model.pagination.Pagination
import com.example.petadopt.animals.domain.usecases.RequestNextPageOfAnimalsUseCase
import com.example.petadopt.animals.presentation.utils.Event
import com.example.petadopt.animals.utils.createExceptionHandler
import com.example.petadopt.logging.Logger
import com.example.petadopt.animals.presentation.model.mappers.UiAnimalMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalsNearYouViewModel @Inject constructor(
    private val uiAnimalMapper: UiAnimalMapper,
    private val compositeDisposable: CompositeDisposable,
    private val requestNextPageOfAnimalsUseCase: RequestNextPageOfAnimalsUseCase
    ) : ViewModel() {

    private val _state = MutableStateFlow(AnimalsNearYouViewState())
    val state: StateFlow<AnimalsNearYouViewState> =
        _state.asStateFlow()

    private var currentPage = 0

    fun onEvent(event: AnimalsNearYouEvent) {
        when (event) {
            is AnimalsNearYouEvent.RequestInitialAnimalsList -> loadAnimals()
        }
    }

    private fun loadAnimals() {
        if (state.value.animals.isEmpty()) { // 1
            loadNextAnimalPage()
        }
    }

    private fun loadNextAnimalPage() {
        val errorMessage = "Failed to fetch nearby animals"
        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            Logger.d("Requesting more animals.")
            val pagination = requestNextPageOfAnimalsUseCase(++currentPage)
            onPaginationInfoObtained(pagination)
        }
    }

    private fun onPaginationInfoObtained(pagination: Pagination) {
        currentPage = pagination.currentPage
    }

    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException,
            is NetworkUnavailableException -> {
                _state.update { oldState ->
                    oldState.copy(
                        loading = false,
                        failure = Event(failure)
                    )
                }
            }
            is NoMoreAnimalsException -> {
                _state.update { oldState ->
                    oldState.copy(
                        noMoreAnimalsNearby = true,
                        failure = Event(failure)
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}