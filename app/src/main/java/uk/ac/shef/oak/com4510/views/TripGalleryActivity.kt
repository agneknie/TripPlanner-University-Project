package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripGalleryBinding

/**
 * Class TripGalleryActivity.
 *
 * Enables user to browse all of their trips. Forwards to the TripOverview activity
 * when a trip is selected to display data of that trip.
 */
class TripGalleryActivity: TripPlannerAppCompatActivity()  {
    private lateinit var binding: ActivityTripGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}