package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoDetailsBinding

class PhotoDetailsActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Insert map into activity_photo_details_ll_map_holder. Amend height as necessary
    }
}