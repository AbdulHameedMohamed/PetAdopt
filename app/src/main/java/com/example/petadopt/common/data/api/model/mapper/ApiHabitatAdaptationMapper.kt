package com.example.petadopt.common.data.api.model.mapper

import com.example.petadopt.common.data.api.model.ApiEnvironment
import com.example.petadopt.common.domain.model.animal.details.HabitatAdaptation
import javax.inject.Inject

class ApiHabitatAdaptationMapper @Inject constructor() :
    ApiMapper<ApiEnvironment?, HabitatAdaptation> {

    override fun mapToDomain(apiEntity: ApiEnvironment?): HabitatAdaptation {
        return HabitatAdaptation(
            goodWithChildren = apiEntity?.children ?: true,
            goodWithDogs = apiEntity?.dogs ?: true,
            goodWithCats = apiEntity?.cats ?: true
        )
    }
}
