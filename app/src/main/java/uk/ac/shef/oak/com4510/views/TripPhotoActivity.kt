package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripPhotoBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys

class TripPhotoActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gets the selected or captured photo
        val bundle = intent.extras
        if(bundle != null){

            // Gets picked photo's uri
            val photoUri = bundle.getString(IntentKeys.PHOTO_URI)

            // TODO Populates photo view
        }
    }
}