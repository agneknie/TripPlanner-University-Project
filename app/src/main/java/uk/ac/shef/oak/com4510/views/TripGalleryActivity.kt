package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripGalleryBinding

class TripGalleryActivity: TripPlannerAppCompatActivity()  {
    private lateinit var binding: ActivityTripGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}