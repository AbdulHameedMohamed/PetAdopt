package com.example.petadopt.main.presentation

sealed class MainActivityEvent {
    data object DefineStartDestination : MainActivityEvent()
}