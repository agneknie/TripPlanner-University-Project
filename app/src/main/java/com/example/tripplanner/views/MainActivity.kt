package com.example.tripplanner.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityMainBinding
import com.example.tripplanner.utilities.Permissions
import com.google.android.material.snackbar.Snackbar

class MainActivity : TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Displays snackbar, if trip has finished in TripTripActivity
    private val tripTripActivityResultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        this.displaySnackbar(binding.root, R.string.trip_finished_successfully_snackbar, Snackbar.LENGTH_LONG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialises button click listeners
        initialiseClickListeners()

        // Checks & requests permissions
        Permissions.checkAndRequestPermissions(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Permissions.onRequestPermissionsCallback(this, binding.root, requestCode)
    }

    /**
     * Initialises click listeners for New Trip, Photo Gallery,
     * Photo Map & Past Trips buttons.
     */
    private fun initialiseClickListeners(){
        // New Trip Button
       binding.activityMainBtnNewTrip.setOnClickListener {
            tripTripActivityResultContract.launch(Intent(this, TripCreationActivity::class.java))
        }

        // Photo Gallery Button
        binding.activityMainBtnPhotoGallery.setOnClickListener{
            startActivity(Intent(this, PhotoGalleryActivity::class.java))
        }

        // Photo Map Button
        binding.activityMainBtnPhotoMap.setOnClickListener{
            startActivity(Intent(this, PhotoMapActivity::class.java))
        }

        // Past Trips Button
        binding.activityMainBtnPastTrips.setOnClickListener{
            startActivity(Intent(this, TripGalleryActivity::class.java))
        }
    }

}