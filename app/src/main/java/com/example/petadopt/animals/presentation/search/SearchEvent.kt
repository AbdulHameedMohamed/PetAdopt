package com.example.petadopt.animals.presentation.search

sealed class SearchEvent {
    data object PrepareForSearch : SearchEvent()
    data class AgeValueSelected(val age: String) : SearchEvent()
    data class TypeValueSelected(val type: String) : SearchEvent()
}