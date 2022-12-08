package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoDisplayBinding

class PhotoDisplayActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}