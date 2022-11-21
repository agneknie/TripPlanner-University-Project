package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityTripPhotoBinding

class TripPhotoActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}