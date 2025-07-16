package com.example.animals.presentation.animal_details

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.animals.R
import com.example.common.R.string.an_error_occurred
import com.example.animals.databinding.FragmentDetailsBinding
import com.example.animals.presentation.animal_details.model.UIAnimalDetailed
import com.example.common.utils.setImage
import com.example.common.utils.toEnglish
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimalDetailsFragment : Fragment() {

    companion object {
        const val ANIMAL_ID = "id"
    }

    private val binding get() = _binding!!
    private var _binding: FragmentDetailsBinding? = null

    private val viewModel: AnimalDetailsViewModel by viewModels()

    private var animalId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animalId = requireArguments().getLong(ANIMAL_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addShareMenu()
        subscribeToStateUpdates()
        val event = AnimalDetailsEvent.LoadAnimalDetails(animalId!!)
        viewModel.handleEvent(event)
    }

    private fun addShareMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_share, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.share) {
                    navigateToSharing()
                    true
                } else {
                    false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun navigateToSharing() {
        val animalId = requireArguments().getLong(ANIMAL_ID)
        val directions =
            AnimalDetailsFragmentDirections.actionDetailsToSharing(animalId)
        findNavController().navigate(directions)
    }

    private fun subscribeToStateUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is AnimalDetailsViewState.Loading -> {
                            displayLoading()
                        }

                        is AnimalDetailsViewState.Failure -> {
                            displayError()
                        }

                        is AnimalDetailsViewState.AnimalDetails -> {
                            displayPetDetails(state.animal)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun displayPetDetails(animalDetails: UIAnimalDetailed) {
        binding.group.isVisible = true
        stopAnimation()
        binding.name.text = animalDetails.name
        binding.description.text = animalDetails.description
        binding.ivImage.setImage(animalDetails.photo)
        binding.tvSprayedNeutered.text = animalDetails.sprayNeutered.toEnglish()
        binding.tvSpecialNeeds.text = animalDetails.specialNeeds.toEnglish()

        val doubleTapGestureListener = object: GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                return true
            }

            override fun onDown(e: MotionEvent) = true
        }
        val doubleTapGestureDetector = GestureDetector(requireContext(), doubleTapGestureListener)

        binding.ivImage.setOnTouchListener { v, event ->
            Log.d("hitler", "displayPetDetails: ")
            doubleTapGestureDetector.onTouchEvent(event)
        }
    }

    private fun displayError() {
        startAnimation()
        binding.group.isVisible = false
        Snackbar.make(requireView(), an_error_occurred, Snackbar.LENGTH_SHORT).show()
    }

    private fun displayLoading() {
        startAnimation()
        binding.group.isVisible = false
    }

    private fun startAnimation() {
        Log.d("hitler", "startAnimation: ")
        binding.loader.isVisible = true
    }

    private fun stopAnimation() {
        Log.d("hitler", "stopAnimation: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}