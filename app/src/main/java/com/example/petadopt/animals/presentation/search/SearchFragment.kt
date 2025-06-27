package com.example.petadopt.animals.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petadopt.animals.presentation.animals_near_you.AnimalsAdapter
import com.example.petadopt.databinding.FragmentSearchBinding
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
        viewModel.onEvent(SearchEvent.PrepareForSearch)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}