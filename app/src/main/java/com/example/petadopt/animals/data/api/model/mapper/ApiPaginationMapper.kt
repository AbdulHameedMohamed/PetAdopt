package com.example.petadopt.animals.data.api.model.mapper

import com.example.petadopt.animals.data.api.model.ApiPagination
import com.example.petadopt.animals.domain.model.pagination.Pagination
import javax.inject.Inject

class ApiPaginationMapper @Inject constructor() : ApiMapper<ApiPagination?, Pagination> {

    override fun mapToDomain(apiEntity: ApiPagination?): Pagination {
        return Pagination(
            currentPage = apiEntity?.currentPage ?: 0,
            totalPages = apiEntity?.totalPages ?: 0
        )
    }
}