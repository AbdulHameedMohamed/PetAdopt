package com.example.petadopt.animals.data.api.model.mapper

interface ApiMapper<E, D> {
    fun mapToDomain(apiEntity: E): D
}