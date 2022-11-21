package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityTripGalleryBinding

class TripGalleryActivity: TripPlannerAppCompatActivity()  {
    private lateinit var binding: ActivityTripGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}