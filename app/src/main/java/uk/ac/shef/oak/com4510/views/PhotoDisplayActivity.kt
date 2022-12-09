package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoDisplayBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys

/**
 * Class PhotoDisplayActivity.
 *
 * Displays a selected photo as the full photo.
 */
class PhotoDisplayActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gets selected photo's id
        val bundle = intent.extras
        if(bundle != null){
            val photoId = bundle.getInt(IntentKeys.SELECTED_PHOTO_ID)

            // Displays the photo
            tripPlannerViewModel.getPhoto(photoId).observe(this){
                binding.activityPhotoDisplayIvPhoto.setImageURI(it.photoPath)
            }
        }
        else finish()
    }
}