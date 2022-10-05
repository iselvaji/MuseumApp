package com.qardio.museum.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.qardio.museum.R
import com.qardio.museum.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity (Entry point) of the application.
 * Container for the [ArtListFragment] and [ArtDetailsFragment] fragments.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        val navFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navHostController = navFragment.findNavController()
        setupActionBarWithNavController(navHostController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navHostController.navigateUp()
    }
}