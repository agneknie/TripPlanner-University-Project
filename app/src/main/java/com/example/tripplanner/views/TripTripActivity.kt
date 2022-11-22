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

        // TODO Insert map into activity_trip_trip_ll_map_holder. Set inserted map's height & width to match parent.
    }
}