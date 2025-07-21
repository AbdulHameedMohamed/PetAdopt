package com.example.animals.presentation.animal_details

import com.example.animals.presentation.animal_details.model.UIAnimalDetailed

sealed class AnimalDetailsViewState {
    data object Loading : AnimalDetailsViewState()

    data class AnimalDetails(
        val animal: UIAnimalDetailed,
        val adopted: Boolean = false
    ) : AnimalDetailsViewState()

    data object Failure : AnimalDetailsViewState()
}