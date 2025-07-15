package com.example.animals.presentation.animal_details.model

data class UIAnimalDetailed(
    val id: Long,
    val name: String,
    val photo: String,
    val description: String,
    val sprayNeutered: Boolean,
    val specialNeeds: Boolean,
    val tags: List<String>,
    val phone: String
)