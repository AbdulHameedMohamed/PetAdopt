package com.example.petadopt.animals.data.model.mapper

import com.example.petadopt.animals.data.model.ApiColors
import com.example.petadopt.animals.domain.model.animal.details.Colors
import javax.inject.Inject

class ApiColorsMapper @Inject constructor() : ApiMapper<ApiColors?, Colors> {

    override fun mapToDomain(apiEntity: ApiColors?): Colors {
        return Colors(
            primary = apiEntity?.primary.orEmpty(),
            secondary = apiEntity?.secondary.orEmpty(),
            tertiary = apiEntity?.tertiary.orEmpty()
        )
    }
}
