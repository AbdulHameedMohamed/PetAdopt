package com.example.petadopt.animals.data.model.mapper

interface ApiMapper<E, D> {
    fun mapToDomain(apiEntity: E): D
}