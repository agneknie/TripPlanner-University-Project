package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityPhotoDetailsBinding

class PhotoDetailsActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Insert map into activity_photo_details_ll_map_holder. Amend height as necessary
    }
}