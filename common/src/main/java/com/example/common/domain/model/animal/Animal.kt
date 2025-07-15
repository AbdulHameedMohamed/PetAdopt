package com.example.common.domain.model.animal

import com.example.common.domain.model.animal.details.Details
import com.example.common.domain.model.organization.Organization
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
) {
    val description: String? = details?.description
    val organizationContact: Organization.Contact? = details?.organizationContact
}