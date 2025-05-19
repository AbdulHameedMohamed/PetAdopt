package com.example.petadopt.animals.data.model.mapper

import com.example.petadopt.animals.data.model.ApiVideoLink
import com.example.petadopt.animals.domain.model.animal.Media
import javax.inject.Inject

class ApiVideoMapper @Inject constructor() : ApiMapper<ApiVideoLink?, Media.Video> {

    override fun mapToDomain(apiEntity: ApiVideoLink?): Media.Video {
        return Media.Video(video = apiEntity?.embed.orEmpty())
    }
}
