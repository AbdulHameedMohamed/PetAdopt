package com.example.sharing.presentation

import com.example.sharing.presentation.model.UIAnimalToShare

data class SharingViewState(
    val animalToShare: UIAnimalToShare = UIAnimalToShare(image = "", defaultMessage = "")
)