package com.example.petadopt.animals.domain.model.animal

import com.example.petadopt.animals.domain.model.animal.details.Details
import org.threeten.bp.LocalDateTime

data class Animal(
    val id: Long,
    val name: String,
    val type: String,
    val details: Details?= null,
    val media: Media,
    val tags: List<String>,
    val adoptionStatus: AdoptionStatus,
    val publishedAt: LocalDateTime
)