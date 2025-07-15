package com.example.animals.presentation.animal_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animals.presentation.animal_details.model.mappers.UiAnimalDetailsMapper
import com.example.common.domain.model.animal.Animal
import com.example.common.domain.usecases.GetAnimalDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalDetailsViewModel @Inject constructor(
    private val uiAnimalDetailsMapper: UiAnimalDetailsMapper,
    private val getAnimalDetails: GetAnimalDetailsUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<AnimalDetailsViewState> =
        MutableStateFlow(AnimalDetailsViewState.Loading)

    val state: StateFlow<AnimalDetailsViewState> get() = _state.asStateFlow()

    fun handleEvent(event: AnimalDetailsEvent) {
        when (event) {
            is AnimalDetailsEvent.LoadAnimalDetails -> subscribeToAnimalDetails(event.animalId)
        }
    }

    private fun subscribeToAnimalDetails(animalId: Long) {
        viewModelScope.launch {
            try {
                val animal = getAnimalDetails(animalId)

                onAnimalsDetails(animal)
            } catch (t: Throwable) {
                onFailure(t)
            }
        }
    }

    private fun onAnimalsDetails(animal: Animal) {
        val animalDetails = uiAnimalDetailsMapper.mapToView(animal)
        _state.update { AnimalDetailsViewState.AnimalDetails(animalDetails) }
    }

    private fun onFailure(failure: Throwable) {
        _state.update { AnimalDetailsViewState.Failure }
    }
}