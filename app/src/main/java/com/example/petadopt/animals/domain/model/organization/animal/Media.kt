package com.example.petadopt.animals.domain.model.organization.animal

data class Media(
    val photos: List<Photo>,
    val videos: List<Video>
) {

    data class Photo(
        val medium: String,
        val full: String
    )

    data class Video(val video: String)
}
