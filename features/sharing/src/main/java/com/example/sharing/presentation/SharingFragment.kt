package com.example.sharing.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.utils.setImage
import com.example.petadopt.di.SharingModuleDependencies
import com.example.sharing.databinding.FragmentSharingBinding
import com.example.sharing.di.DaggerSharingComponent
import com.example.sharing.di.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import javax.inject.Inject

class SharingFragment : Fragment() {

  companion object {
    const val ANIMAL_ID = "id"
  }

  private val binding get() = _binding!!
  private var _binding: FragmentSharingBinding? = null

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  private val viewModel by viewModels<SharingViewModel> { viewModelFactory }

  override fun onCreate(savedInstanceState: Bundle?) {
    DaggerSharingComponent.builder()
      .context(requireActivity())
      .moduleDependencies(
        EntryPointAccessors.fromApplication(
          requireActivity().applicationContext,
          SharingModuleDependencies::class.java
        )
      )
      .build()
      .inject(this)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentSharingBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupUI()
    subscribeToViewStateUpdates()
  }

  private fun setupUI() {
    val animalId = requireArguments().getLong(ANIMAL_ID)
    viewModel.onEvent(SharingEvent.GetAnimalToShare(animalId))

    binding.btnShare.setOnClickListener {
      Snackbar.make(requireView(), "Shared! Or not :]", Snackbar.LENGTH_SHORT)
          .show()
    }
  }

  private fun subscribeToViewStateUpdates() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.viewState.collect { render(it) }
      }
    }
  }

  private fun render(viewState: SharingViewState) {
    val (image, message) = viewState.animalToShare

    binding.ivImage.setImage(image)
    binding.etMessageToShare.setText(message)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}