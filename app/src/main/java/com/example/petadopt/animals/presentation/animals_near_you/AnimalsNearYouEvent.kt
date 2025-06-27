package com.example.petadopt.animals.presentation.animals_near_you

sealed class AnimalsNearYouEvent {
    data object RequestInitialAnimalsList : AnimalsNearYouEvent()
    data object RequestMoreAnimals: AnimalsNearYouEvent()
}