package com.example.search.presentation

sealed class SearchEvent {
    data object PrepareForSearch : SearchEvent()
    data class QueryInput(val input: String) : SearchEvent()
    data class AgeValueSelected(val age: String) : SearchEvent()
    data class TypeValueSelected(val type: String) : SearchEvent()
}