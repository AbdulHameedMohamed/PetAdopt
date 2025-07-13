package com.example.onboarding.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.onboarding.R
import com.example.onboarding.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewStateUpdates()
        observeViewEffects()
    }

    private fun setupUI() {
        setupPostcodeTextField()
        setupDistanceTextField()
        listenToSubmitButton()
    }

    private fun setupPostcodeTextField() {
        binding.etPostcode.doAfterTextChanged {
            viewModel.onEvent(OnboardingEvent.PostcodeChanged(it!!.toString()))
        }
    }

    private fun setupDistanceTextField() {
        binding.etMaxDistance.doAfterTextChanged {
            viewModel.onEvent(OnboardingEvent.DistanceChanged(it!!.toString()))
        }
    }

    private fun listenToSubmitButton() {
        binding.btnOnboardingSubmit.setOnClickListener {
            viewModel.onEvent(OnboardingEvent.SubmitButtonClicked)
        }
    }

    private fun observeViewStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { render(it) }
            }
        }
    }

    private fun render(state: OnboardingViewState) {
        with(binding) {
            tiPostcode.error = resources.getString(state.postcodeError)
            tiMaxDistance.error = resources.getString(state.distanceError)
            btnOnboardingSubmit.isEnabled = state.submitButtonActive
        }
    }

    private fun observeViewEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffects.collect { reactTo(it) }
            }
        }
    }

    private fun reactTo(effect: OnboardingViewEffect) {
        when (effect) {
            is OnboardingViewEffect.NavigateToAnimalsNearYou -> navigateToAnimalsNearYou()
        }
    }

    private fun navigateToAnimalsNearYou() {
        val deepLink = NavDeepLinkRequest.Builder
            .fromUri("petadopt://animalsnearyou".toUri())
            .build()

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_onboarding, true)
            //.setEnterAnim(R.anim.nav_default_enter_anim)
            //.setExitAnim(R.anim.nav_default_exit_anim)
            .build()

        findNavController().navigate(deepLink, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}