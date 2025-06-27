package com.example.petadopt.animals.presentation.model.mappers

interface UiMapper<E, V> {

    fun mapToView(input: E): V
}