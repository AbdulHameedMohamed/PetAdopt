package com.example.onboarding.presentation

sealed class OnboardingEvent {
    data class PostcodeChanged(val newPostcode: String) : OnboardingEvent()
    data class DistanceChanged(val newDistance: String) : OnboardingEvent()
    data object SubmitButtonClicked : OnboardingEvent()
}