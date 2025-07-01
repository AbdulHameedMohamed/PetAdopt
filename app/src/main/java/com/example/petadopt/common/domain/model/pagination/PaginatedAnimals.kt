
package com.example.petadopt.common.domain.model.pagination

import com.example.petadopt.common.domain.model.animal.Animal

data class PaginatedAnimals(
    val animals: List<Animal>,
    val pagination: Pagination
)