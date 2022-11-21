package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityPhotoDisplayBinding

class PhotoDisplayActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}