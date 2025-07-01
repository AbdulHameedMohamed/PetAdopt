package com.example.petadopt.animals.presentation

sealed class AnimalsNearYouEvent {
    data object RequestInitialAnimalsList : AnimalsNearYouEvent()
    data object RequestMoreAnimals: AnimalsNearYouEvent()
}