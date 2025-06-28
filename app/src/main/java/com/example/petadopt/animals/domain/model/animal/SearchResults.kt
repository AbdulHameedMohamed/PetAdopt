package com.example.petadopt.animals.domain.model.animal

data class SearchResults(
    val animals: List<Animal>,
    val searchParameters: SearchParameters
)