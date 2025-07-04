package com.example.common.data.api.model.mapper

import com.example.common.data.api.model.ApiPhotoSizes
import com.example.common.domain.model.animal.Media
import javax.inject.Inject

class ApiPhotoMapper @Inject constructor() : ApiMapper<ApiPhotoSizes?, com.example.common.domain.model.animal.Media.Photo> {

    override fun mapToDomain(apiEntity: ApiPhotoSizes?): com.example.common.domain.model.animal.Media.Photo {
        return com.example.common.domain.model.animal.Media.Photo(
            medium = apiEntity?.medium.orEmpty(),
            full = apiEntity?.full.orEmpty()
        )
    }
}