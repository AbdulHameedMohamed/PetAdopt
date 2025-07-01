package com.example.petadopt.common.data.api.model.mapper

import com.example.petadopt.common.data.api.model.ApiPhotoSizes
import com.example.petadopt.common.domain.model.animal.Media
import javax.inject.Inject

class ApiPhotoMapper @Inject constructor() : ApiMapper<ApiPhotoSizes?, com.example.petadopt.common.domain.model.animal.Media.Photo> {

    override fun mapToDomain(apiEntity: ApiPhotoSizes?): com.example.petadopt.common.domain.model.animal.Media.Photo {
        return com.example.petadopt.common.domain.model.animal.Media.Photo(
            medium = apiEntity?.medium.orEmpty(),
            full = apiEntity?.full.orEmpty()
        )
    }
}