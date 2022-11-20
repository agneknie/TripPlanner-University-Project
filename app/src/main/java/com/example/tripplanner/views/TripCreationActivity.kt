package com.example.tripplanner.views

import android.os.Bundle
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.components.TagsPanel
import com.example.tripplanner.databinding.ActivityTripCreationBinding

class TripCreationActivity: TripPlannerAppCompatActivity(){
    private lateinit var binding: ActivityTripCreationBinding

    private lateinit var tagsPanel: TagsPanel

    override fun onCreate(savedInstanceState: Bundle?){
        // Activity binding & layout configuration
        super.onCreate(savedInstanceState)
        binding = ActivityTripCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tag Panel configuration
        tagsPanel = TagsPanel(
            this,
            binding,
            tripPlannerViewModel)
    }
}