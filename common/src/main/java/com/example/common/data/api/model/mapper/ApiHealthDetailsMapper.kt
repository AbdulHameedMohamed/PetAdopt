package com.example.common.data.api.model.mapper

import com.example.common.data.api.model.ApiAttributes
import com.example.common.domain.model.animal.details.HealthDetails
import javax.inject.Inject

class ApiHealthDetailsMapper @Inject constructor() : ApiMapper<ApiAttributes?, HealthDetails> {

    override fun mapToDomain(apiEntity: ApiAttributes?): HealthDetails {
        return HealthDetails(
            isSpayedOrNeutered = apiEntity?.spayedNeutered ?: false,
            isDeclawed = apiEntity?.declawed ?: false,
            hasSpecialNeeds = apiEntity?.specialNeeds ?: false,
            shotsAreCurrent = apiEntity?.shotsCurrent ?: false
        )
    }
}
