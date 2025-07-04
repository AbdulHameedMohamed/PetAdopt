package com.example.common.data.api.model.mapper

import com.example.common.data.api.model.ApiBreeds
import com.example.common.domain.model.animal.details.Breed
import javax.inject.Inject

class ApiBreedsMapper @Inject constructor() : ApiMapper<ApiBreeds?, Breed> {

    override fun mapToDomain(apiEntity: ApiBreeds?): Breed {
        return Breed(
            primary = apiEntity?.primary.orEmpty(),
            secondary = apiEntity?.secondary.orEmpty()
        )
    }
}