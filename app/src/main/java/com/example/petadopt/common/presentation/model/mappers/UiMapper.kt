package com.example.petadopt.common.presentation.model.mappers

interface UiMapper<E, V> {

    fun mapToView(input: E): V
}