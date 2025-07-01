package com.example.petadopt.animals.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petadopt.R
import com.example.petadopt.common.presentation.AnimalsAdapter
import com.example.petadopt.common.presentation.utils.Event
import com.example.petadopt.databinding.FragmentAnimalsNearYouBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): AnimalsAdapter = AnimalsAdapter()

    private fun setupRecyclerView(animalsNearYouAdapter: AnimalsAdapter) {
        binding.animalsRecyclerView.apply {
            adapter = animalsNearYouAdapter
            setHasFixedSize(true)
            addOnScrollListener(createInfiniteScrollListener(layoutManager as GridLayoutManager))
        }
    }

    private fun createInfiniteScrollListener(
        layoutManager: GridLayoutManager
    ): RecyclerView.OnScrollListener {
        return object : InfiniteScrollListener(
            layoutManager,
            AnimalsNearYouViewModel.UI_PAGE_SIZE
        ) {
            override fun loadMoreItems() {
                requestMoreAnimals()
            }

            override fun isLoading(): Boolean = viewModel.isLoadingMoreAnimals
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    private fun requestMoreAnimals() {
        viewModel.onEvent(AnimalsNearYouEvent.RequestMoreAnimals)
    }

    private fun subscribeToViewStateUpdates(adapter: AnimalsAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateScreenState(it, adapter)
                }
            }
        }
    }

    private fun updateScreenState(
        state: AnimalsNearYouViewState,
        adapter: AnimalsAdapter
    ) {
        binding.progressBar.isVisible = state.loading
        adapter.submitList(state.animals)
        handleNoMoreAnimalsNearby(state.noMoreAnimalsNearby)
        handleFailures(state.failure)
    }

    private fun handleNoMoreAnimalsNearby(noMoreAnimalsNearby: Boolean) {

    }

    private fun handleFailures(failure: Event<Throwable>?) {
        val unhandledFailure = failure?.getContentIfNotHandled() ?: return
        val fallbackMessage = getString(R.string.an_error_occurred)
        val snackbarMessage = if (unhandledFailure.message.isNullOrEmpty()) {
            fallbackMessage
        } else {
            unhandledFailure.message!!
        }
        if (snackbarMessage.isNotEmpty()) {
            Snackbar.make(
                requireView(), snackbarMessage,
                Snackbar.LENGTH_SHORT
            ).show()
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