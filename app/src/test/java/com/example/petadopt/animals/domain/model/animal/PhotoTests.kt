package com.example.petadopt.animals.domain.model.animal

import org.junit.Test
import org.junit.Assert.assertEquals

class PhotoTests {
    private val mediumPhoto = "mediumPhoto"
    private val fullPhoto = "fullPhoto"
    private val invalidPhoto = ""

    @Test
    fun photo_getSmallestAvailablePhoto_hasMediumPhoto() {
        // Given
        val photo = com.example.petadopt.common.domain.model.animal.Media.Photo(mediumPhoto, fullPhoto)
        val expectedValue = mediumPhoto

        // When
        val smallestPhoto = photo.getSmallestAvailablePhoto()

        // Then
        assertEquals(smallestPhoto, expectedValue)
    }

    @Test
    fun photo_getSmallestAvailablePhoto_notHasLargePhoto() {
        // Given
        val photo = com.example.petadopt.common.domain.model.animal.Media.Photo(mediumPhoto, "")
        val expectedValue = mediumPhoto

        // When
        val smallestPhoto = photo.getSmallestAvailablePhoto()

        // Then
        assertEquals(smallestPhoto, expectedValue)
    }

    @Test
    fun photo_getSmallestAvailablePhoto_noMediumPhoto_hasFullPhoto() {
        // Given
        val photo = com.example.petadopt.common.domain.model.animal.Media.Photo(invalidPhoto, fullPhoto)
        val expectedValue = fullPhoto

        // When
        val smallestPhoto = photo.getSmallestAvailablePhoto()

        // Then
        assertEquals(smallestPhoto, expectedValue)
    }

    @Test
    fun photo_getSmallestAvailablePhoto_noPhotos() {
        // Given
        val photo = com.example.petadopt.common.domain.model.animal.Media.Photo(invalidPhoto, invalidPhoto)
        val expectedValue = com.example.petadopt.common.domain.model.animal.Media.Photo.EMPTY_PHOTO

        // When
        val smallestPhoto = photo.getSmallestAvailablePhoto()

        // Then
        assertEquals(smallestPhoto, expectedValue)
    }
}