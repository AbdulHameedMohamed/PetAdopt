package com.example.petadopt.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.createExceptionHandler
import com.example.petadopt.main.domain.usecases.OnboardingIsCompleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingIsComplete: OnboardingIsCompleteUseCase
) : ViewModel() {

    val viewEffect: SharedFlow<MainActivityViewEffect> get() = _viewEffect

    private val _viewEffect = MutableSharedFlow<MainActivityViewEffect>()

    fun onEvent(event: MainActivityEvent) {
        when (event) {
            is MainActivityEvent.DefineStartDestination -> defineStartDestination()
        }
    }

    private fun defineStartDestination() {
        val errorMessage = "Failed to check if onboarding is complete"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            val destination = if (onboardingIsComplete()) {
                com.example.animals.R.id.nav_animalsnearyou
            } else {
                com.example.onboarding.R.id.nav_onboarding
            }

            _viewEffect.emit(MainActivityViewEffect.SetStartDestination(destination))
        }
    }

    private fun onFailure(throwable: Throwable) {
        // TODO: Handle failures
    }
}