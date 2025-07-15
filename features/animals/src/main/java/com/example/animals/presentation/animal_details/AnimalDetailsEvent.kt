package com.example.animals.presentation.animal_details

sealed class AnimalDetailsEvent {
  data class LoadAnimalDetails(val animalId: Long) : AnimalDetailsEvent()
}