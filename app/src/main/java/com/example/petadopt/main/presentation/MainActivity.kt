package com.example.petadopt.main.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.petadopt.R
import com.example.petadopt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            topLevelDestinationIds = setOf(
                com.example.onboarding.R.id.onboardingFragment,
                com.example.animals.R.id.animalsNearYouFragment,
                com.example.search.R.id.searchFragment
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(com.example.common.R.style.AppTheme)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupBottomNav()
        triggerStartDestinationEvent()
        subscribeToViewEffects()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNav() {
        binding.bottomNavigation.setupWithNavController(navController)
        hideBottomNavWhenInOnboarding()
    }

    private fun hideBottomNavWhenInOnboarding() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == com.example.onboarding.R.id.onboardingFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    private fun triggerStartDestinationEvent() {
        viewModel.onEvent(MainActivityEvent.DefineStartDestination)
    }

    private fun subscribeToViewEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffect.collect { reactTo(it) }
            }
        }
    }

    private fun reactTo(effect: MainActivityViewEffect) {
        when (effect) {
            is MainActivityViewEffect.SetStartDestination -> setNavGraphStartDestination(effect.destination)
        }
    }

    private fun setNavGraphStartDestination(startDestination: Int) {
        val navGraph = navController.navInflater.inflate(R.navigation.home_nav_graph)

        navGraph.setStartDestination(startDestination)
        navController.graph = navGraph
    }
}