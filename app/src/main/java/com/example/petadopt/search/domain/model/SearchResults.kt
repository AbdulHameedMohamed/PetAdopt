package com.example.petadopt.search.domain.model

import com.example.petadopt.common.domain.model.animal.Animal

data class SearchResults(
    val animals: List<Animal>,
    val searchParameters: SearchParameters
)