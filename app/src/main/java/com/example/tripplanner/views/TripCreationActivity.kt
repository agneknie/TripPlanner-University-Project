package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity

class TripCreationActivity: TripPlannerAppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_creation)
    }
}