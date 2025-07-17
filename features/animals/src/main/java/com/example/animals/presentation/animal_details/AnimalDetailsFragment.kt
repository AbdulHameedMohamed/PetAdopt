package com.example.animals.presentation.animal_details

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
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
        const val FLING_SCALE = 1f
        const val ANIMAL_ID = "id"
    }

    private val binding get() = _binding!!
    private var _binding: FragmentDetailsBinding? = null

    private val viewModel: AnimalDetailsViewModel by viewModels()

    private var animalId: Long? = null

    private val springForce: SpringForce by lazy {
        SpringForce().apply {
            dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
            stiffness = SpringForce.STIFFNESS_VERY_LOW
        }
    }

    private val callScaleXSpringAnimation: SpringAnimation by lazy {
        SpringAnimation(binding.btnCall, DynamicAnimation.SCALE_X).apply {
            spring = springForce
        }
    }

    private val callScaleYSpringAnimation: SpringAnimation by lazy {
        SpringAnimation(binding.btnCall, DynamicAnimation.SCALE_Y).apply {
            spring = springForce
        }
    }

    private val FLING_FRICTION = 2f

    private val callFlingXAnimation: FlingAnimation by lazy {
        FlingAnimation(binding.btnCall, DynamicAnimation.X).apply {
            friction = FLING_FRICTION
            setMinValue(0f)
            setMaxValue(binding.root.width.toFloat() - binding.btnCall.width.toFloat())
        }
    }

    private val callFlingYAnimation: FlingAnimation by lazy {
        FlingAnimation(binding.btnCall, DynamicAnimation.Y).apply {
            friction = FLING_FRICTION
            setMinValue(0f)
            setMaxValue(binding.root.height.toFloat() - binding.btnCall.width.toFloat())
        }
    }

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
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
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

        val doubleTapGestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                (binding.heartImage.drawable as Animatable?)?.start()
                return true
            }

            override fun onDown(e: MotionEvent) = true
        }
        val doubleTapGestureDetector = GestureDetector(requireContext(), doubleTapGestureListener)

        binding.heartImage.setOnTouchListener { v, event ->
            doubleTapGestureDetector.onTouchEvent(event)
        }

        callScaleXSpringAnimation.animateToFinalPosition(FLING_SCALE)
        callScaleYSpringAnimation.animateToFinalPosition(FLING_SCALE)

        val flingGestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                callFlingXAnimation.setStartVelocity(velocityX).start()
                callFlingYAnimation.setStartVelocity(velocityY).start()
                return true
            }

            override fun onDown(e: MotionEvent) = true
        }
        val flingGestureDetector = GestureDetector(requireContext(), flingGestureListener)

        binding.btnCall.setOnTouchListener { v, event ->
            flingGestureDetector.onTouchEvent(event)
        }

        binding.root.post {
            callFlingXAnimation.setMaxValue(binding.root.width.toFloat() - binding.btnCall.width)
            callFlingYAnimation.setMaxValue(binding.root.height.toFloat() - binding.btnCall.height)

            callFlingYAnimation.addEndListener { _, _, _, _ ->
                if (areViewsOverlapping(binding.btnCall, binding.ivImage)) {
                    val action = AnimalDetailsFragmentDirections.actionDetailsToSecret()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun displayError() {
        startAnimation(R.raw.lazy_cat)
        binding.group.isVisible = false
        Snackbar.make(requireView(), an_error_occurred, Snackbar.LENGTH_SHORT).show()
    }

    private fun displayLoading() {
        startAnimation(R.raw.happy_dog)
        binding.group.isVisible = false
    }

    private fun startAnimation(@RawRes animationRes: Int) {
        binding.loader.apply {
            isVisible = true
            setMinFrame(50)
            setMaxFrame(112)
            speed = 1.2f
            setAnimation(animationRes)
            playAnimation()
        }
        binding.loader.addValueCallback(
            KeyPath("icon_circle", "**"),
            LottieProperty.COLOR_FILTER,
            {
                PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP)
            }
        )
    }

    private fun stopAnimation() {
        binding.loader.apply {
            cancelAnimation()
            isVisible = false
        }
    }

    private fun areViewsOverlapping(view1: View, view2: View): Boolean {
        val firstRect = Rect()
        view1.getHitRect(firstRect)

        val secondRect = Rect()
        view2.getHitRect(secondRect)

        return Rect.intersects(firstRect, secondRect)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}