package com.example.animals.presentation.main

sealed class AnimalsNearYouEvent {
    data object RequestInitialAnimalsList : AnimalsNearYouEvent()
    data object RequestMoreAnimals: AnimalsNearYouEvent()
}