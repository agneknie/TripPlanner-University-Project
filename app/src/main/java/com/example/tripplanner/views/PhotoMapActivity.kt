package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityPhotoMapBinding

class PhotoMapActivity: TripPlannerAppCompatActivity()  {
    private lateinit var binding: ActivityPhotoMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}