package com.example.tripplanner.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity

class MainActivity : TripPlannerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialises button click listeners
        initialiseClickListeners()
    }

    /**
     * Initialises click listeners for New Trip, Photo Gallery,
     * Photo Map & Past Trips buttons.
     */
    private fun initialiseClickListeners(){
        // New Trip Button
        val newTripButton: Button = findViewById(R.id.activity_main_btn_new_trip)
        newTripButton.setOnClickListener {
            startActivity(Intent(this, TripCreationActivity::class.java))
        }

        // Photo Gallery Button
        val photoGalleryButton: Button = findViewById(R.id.activity_main_btn_photo_gallery)
        photoGalleryButton.setOnClickListener{
            startActivity(Intent(this, PhotoGalleryActivity::class.java))
        }

        // Photo Map Button
        val photoMapButton: Button = findViewById(R.id.activity_main_btn_photo_map)
        photoMapButton.setOnClickListener{
            startActivity(Intent(this, PhotoMapActivity::class.java))
        }

        // Past Trips Button
        val pastTripsButton: Button = findViewById(R.id.activity_main_btn_past_trips)
        pastTripsButton.setOnClickListener{
            startActivity(Intent(this, TripGalleryActivity::class.java))
        }
    }

}