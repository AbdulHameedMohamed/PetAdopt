package com.example.petadopt.animals.presentation.animals_near_you

import com.example.petadopt.animals.presentation.model.UIAnimal
import com.example.petadopt.animals.presentation.utils.Event

data class AnimalsNearYouViewState(
    val loading: Boolean = true,
    val animals: List<UIAnimal> = emptyList(),
    val noMoreAnimalsNearby: Boolean = false,
    val failure: Event<Throwable>? = null
)