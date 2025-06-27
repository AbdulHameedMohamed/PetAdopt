package com.example.petadopt.animals.presentation.animals_near_you

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petadopt.animals.domain.NetworkException
import com.example.petadopt.animals.domain.NetworkUnavailableException
import com.example.petadopt.animals.domain.NoMoreAnimalsException
import com.example.petadopt.animals.domain.model.pagination.Pagination
import com.example.petadopt.animals.domain.usecases.GetAnimalsUseCase
import com.example.petadopt.animals.domain.usecases.RequestNextPageOfAnimalsUseCase
import com.example.petadopt.animals.presentation.model.UIAnimal
import com.example.petadopt.animals.presentation.utils.Event
import com.example.petadopt.animals.utils.createExceptionHandler
import com.example.petadopt.logging.Logger
import com.example.petadopt.animals.presentation.model.mappers.UiAnimalMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
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
    private val getAnimalsUseCase: GetAnimalsUseCase,
    private val requestNextPageOfAnimalsUseCase: RequestNextPageOfAnimalsUseCase
) : ViewModel() {

    companion object {
        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
    }

    init {
        subscribeToAnimalUpdates()
    }

    private val _state = MutableStateFlow(AnimalsNearYouViewState())
    val state: StateFlow<AnimalsNearYouViewState> =
        _state.asStateFlow()

    val isLastPage: Boolean
        get() = state.value.noMoreAnimalsNearby
    var isLoadingMoreAnimals: Boolean = false
        private set

    private var currentPage = 0

    fun onEvent(event: AnimalsNearYouEvent) {
        when (event) {
            is AnimalsNearYouEvent.RequestInitialAnimalsList -> loadAnimals()
            is AnimalsNearYouEvent.RequestMoreAnimals -> loadNextAnimalPage()
        }
    }

    private fun subscribeToAnimalUpdates() {
        getAnimalsUseCase().map { animals -> animals.map { uiAnimalMapper.mapToView(it) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNewAnimalList(it) },
                { onFailure(it) }
            ).addTo(compositeDisposable)
    }

    private fun onNewAnimalList(animals: List<UIAnimal>) {
        Logger.d("Got more animals!")

        val updatedAnimalSet = (state.value.animals + animals).toSet()

        Log.d("hitler", "onNewAnimalList: $updatedAnimalSet")
        _state.update { oldState ->
            oldState.copy(
                loading = false,
                animals = updatedAnimalSet.toList()
            )
        }
    }

    private fun loadAnimals() {
        if (state.value.animals.isEmpty()) {
            loadNextAnimalPage()
        }
    }

    private fun loadNextAnimalPage() {
        isLoadingMoreAnimals = true

        val errorMessage = "Failed to fetch nearby animals"
        val exceptionHandler =
            viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            Logger.d("Requesting more animals.")
            val pagination = requestNextPageOfAnimalsUseCase(++currentPage)
            onPaginationInfoObtained(pagination)
            isLoadingMoreAnimals = false
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