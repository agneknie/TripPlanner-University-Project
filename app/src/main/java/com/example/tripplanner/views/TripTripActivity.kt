package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityTripTripBinding

class TripTripActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}