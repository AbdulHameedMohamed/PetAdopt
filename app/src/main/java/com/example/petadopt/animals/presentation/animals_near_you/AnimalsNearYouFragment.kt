package com.example.petadopt.animals.presentation.animals_near_you

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.petadopt.R
import com.example.petadopt.databinding.FragmentAnimalsNearYouBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimalsNearYouFragment : Fragment() {
    private val viewModel: AnimalsNearYouViewModel by viewModels()

    private var _binding: FragmentAnimalsNearYouBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalsNearYouBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        requestInitialAnimalsList()
    }

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
    }

    private fun createAdapter(): AnimalsAdapter = AnimalsAdapter()

    private fun setupRecyclerView(animalsNearYouAdapter: AnimalsAdapter) {
        binding.animalsRecyclerView.apply {
            adapter = animalsNearYouAdapter
            setHasFixedSize(true)
        }
    }

    private fun requestInitialAnimalsList() {
        viewModel.onEvent(AnimalsNearYouEvent.RequestInitialAnimalsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}