package com.example.sharing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.domain.usecases.GetAnimalDetailsUseCase
import com.example.sharing.presentation.model.mapper.UiAnimalToShareMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class SharingViewModel @Inject constructor(
    private val getAnimalDetails: GetAnimalDetailsUseCase,
    private val uiAnimalToShareMapper: UiAnimalToShareMapper
): ViewModel() {

  val viewState: StateFlow<SharingViewState> get() = _viewState

  private val _viewState = MutableStateFlow(SharingViewState())

  fun onEvent(event: SharingEvent) {
    when (event) {
      is SharingEvent.GetAnimalToShare -> getAnimalToShare(event.animalId)
    }
  }

  private fun getAnimalToShare(animalId: Long) {
    viewModelScope.launch {
      val animal = getAnimalDetails(animalId)

      _viewState.update { oldState ->
        oldState.copy(animalToShare = uiAnimalToShareMapper.mapToView(animal))
      }
    }
  }
}