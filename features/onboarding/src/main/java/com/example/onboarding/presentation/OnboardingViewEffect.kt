package com.example.onboarding.presentation

sealed class OnboardingViewEffect {
    data object NavigateToAnimalsNearYou : OnboardingViewEffect()
}