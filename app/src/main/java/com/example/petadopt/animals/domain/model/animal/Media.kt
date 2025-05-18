package com.example.petadopt.animals.domain.model.animal

data class Media(
    val photos: List<Photo>,
    val videos: List<Video>
) {

    data class Photo(
        val medium: String,
        val full: String
    ) {
        companion object {
            const val EMPTY_PHOTO = ""
        }

        // for the Recycler View need the smallest image
        fun getSmallestAvailablePhoto(): String {
            return when {
                isValidPhoto(medium) -> medium
                isValidPhoto(full) -> full
                else -> EMPTY_PHOTO
            }
        }

        private fun isValidPhoto(photo: String): Boolean {
            return photo.isNotEmpty()
        }
    }

    data class Video(val video: String)
}
