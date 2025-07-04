package com.example.animals.presentation

import com.example.common.presentation.model.UIAnimal
import com.example.common.presentation.utils.Event

data class AnimalsNearYouViewState(
    val loading: Boolean = true,
    val animals: List<UIAnimal> = emptyList(),
    val noMoreAnimalsNearby: Boolean = false,
    val failure: Event<Throwable>? = null
)