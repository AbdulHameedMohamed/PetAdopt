package com.example.petadopt.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petadopt.R
import com.example.petadopt.search.domain.usecase.GetSearchFiltersUseCase
import com.example.common.presentation.AnimalsAdapter
import com.example.common.presentation.utils.Event
import com.example.petadopt.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        prepareForSearch()
    }

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        prepareForSearch()
        observeViewStateUpdates(adapter)
    }

    private fun createAdapter(): AnimalsAdapter = AnimalsAdapter()

    private fun setupRecyclerView(animalsNearYouAdapter: AnimalsAdapter) {
        binding.rvSearch.apply {
            adapter = animalsNearYouAdapter
            setHasFixedSize(true)
        }
    }

    private fun prepareForSearch() {
        setupFilterListeners()
        setupSearchViewListener()
        viewModel.onEvent(SearchEvent.PrepareForSearch)
    }

    private fun observeViewStateUpdates(adapter: AnimalsAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreenState(it, adapter)
        }
    }

    private fun setupSearchViewListener() {
        val searchView = binding.wSearch.tvSearch

        searchView.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.onEvent(SearchEvent.QueryInput(query.orEmpty()))
                    searchView.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onEvent(SearchEvent.QueryInput(newText.orEmpty()))
                    return true
                }

            }
        )
    }

    private fun setupFilterListeners() {
        with(binding.wSearch) {
            setupFilterListenerFor(acAge) { item ->
                viewModel.onEvent(SearchEvent.AgeValueSelected(item))
            }
            setupFilterListenerFor(acType) { item ->
                viewModel.onEvent(SearchEvent.TypeValueSelected(item))
            }
        }
    }

    private fun setupFilterListenerFor(
        filter: AutoCompleteTextView,
        block: (item: String) -> Unit
    ) {
        filter.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            parent?.let {
                block(it.adapter.getItem(position) as String)
            }
        }
    }

    private fun updateScreenState(
        newState: SearchViewState,
        searchAdapter: AnimalsAdapter
    ) {
        val (
            inInitialState,
            searchResults,
            ageFilterValues,
            typeFilterValues,
            searchingRemotely,
            noResultsState,
            failure
        ) = newState

        updateInitialStateViews(inInitialState)
        searchAdapter.submitList(searchResults)

        with (binding.wSearch) {
            setupFilterValues(
                acAge,
                ageFilterValues.getContentIfNotHandled()
            )
            setupFilterValues(
                acType,
                typeFilterValues.getContentIfNotHandled()
            )
        }
        updateNoResultsView(noResultsState)
        updateRemoteSearchViews(searchingRemotely)
        handleFailures(failure)
    }

    private fun updateRemoteSearchViews(searchingRemotely: Boolean) {
        binding.pbSearchRemotely.isVisible = searchingRemotely
        binding.tvSearchRemotely.isVisible = searchingRemotely
    }

    private fun updateNoResultsView(noResultsState: Boolean) {
        binding.ivNoSearchResults.isVisible = noResultsState
        binding.tvNoSearchResults.isVisible = noResultsState
    }

    private fun setupFilterValues(
        filter: AutoCompleteTextView,
        filterValues: List<String>?
    ) {
        if (filterValues.isNullOrEmpty()) return
        filter.setAdapter(createFilterAdapter(filterValues))
        filter.setText(GetSearchFiltersUseCase.NO_FILTER_SELECTED, false)
    }

    private fun createFilterAdapter(
        adapterValues: List<String>
    ): ArrayAdapter<String> {
        return ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            adapterValues
        )
    }

    private fun updateInitialStateViews(inInitialState: Boolean) {
        binding.ivInitialSearch.isVisible = inInitialState
        binding.tvInitialSearch.isVisible = inInitialState
    }

    private fun handleFailures(failure: Event<Throwable>?) {
        val unhandledFailure = failure?.getContentIfNotHandled() ?: return

        val fallbackMessage = getString(R.string.an_error_occurred)
        val snackbarMessage = if (unhandledFailure.message.isNullOrEmpty()) {
            fallbackMessage
        }
        else {
            unhandledFailure.message!!
        }

        if (snackbarMessage.isNotEmpty()) {
            Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}