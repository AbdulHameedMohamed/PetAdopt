
package com.example.petadopt.animals.domain.model.pagination

import com.example.petadopt.animals.domain.model.animal.Animal

data class PaginatedAnimals(
    val animals: List<Animal>,
    val pagination: Pagination
)