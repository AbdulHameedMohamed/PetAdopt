
package com.example.common.domain.model.pagination

import com.example.common.domain.model.animal.Animal

data class PaginatedAnimals(
    val animals: List<Animal>,
    val pagination: Pagination
)