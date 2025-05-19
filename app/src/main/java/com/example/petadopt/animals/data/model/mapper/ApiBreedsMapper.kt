package com.example.petadopt.animals.data.model.mapper

import com.example.petadopt.animals.data.model.ApiBreeds
import com.example.petadopt.animals.domain.model.animal.details.Breed
import javax.inject.Inject

class ApiBreedsMapper @Inject constructor() : ApiMapper<ApiBreeds?, Breed> {

    override fun mapToDomain(apiEntity: ApiBreeds?): Breed {
        return Breed(
            primary = apiEntity?.primary.orEmpty(),
            secondary = apiEntity?.secondary.orEmpty()
        )
    }
}