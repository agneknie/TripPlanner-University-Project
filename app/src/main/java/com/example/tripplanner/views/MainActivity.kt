package com.example.tripplanner.views

import android.content.Intent
import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityMainBinding

class MainActivity : TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialises button click listeners
        initialiseClickListeners()
    }

    /**
     * Initialises click listeners for New Trip, Photo Gallery,
     * Photo Map & Past Trips buttons.
     */
    private fun initialiseClickListeners(){
        // New Trip Button
       binding.activityMainBtnNewTrip.setOnClickListener {
            startActivity(Intent(this, TripCreationActivity::class.java))
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