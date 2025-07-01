package com.example.petadopt.common.data.api.model.mapper

import com.example.petadopt.common.data.api.model.ApiVideoLink
import com.example.petadopt.common.domain.model.animal.Media
import javax.inject.Inject

class ApiVideoMapper @Inject constructor() : ApiMapper<ApiVideoLink?, Media.Video> {

    override fun mapToDomain(apiEntity: ApiVideoLink?): com.example.petadopt.common.domain.model.animal.Media.Video {
        return com.example.petadopt.common.domain.model.animal.Media.Video(video = apiEntity?.embed.orEmpty())
    }
}
