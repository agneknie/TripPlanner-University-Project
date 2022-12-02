package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoMapBinding

class PhotoMapActivity: TripPlannerAppCompatActivity()  {
    private lateinit var binding: ActivityPhotoMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}