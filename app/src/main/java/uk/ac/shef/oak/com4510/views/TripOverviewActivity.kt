package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripOverviewBinding

/**
 * Class TripOverviewActivity.
 *
 * When a Trip is selected from the Trip Gallery, displays the Trip's
 * data and allows further interaction with its elements.
 */
class TripOverviewActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}