package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoGalleryBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys

class PhotoGalleryActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Remove/Change. Placeholder so I can test the PhotoDisplay & PhotoDetails activities behaviour
        binding.activityPhotoGalleryIvSorting.setOnClickListener {
            onPhotoClicked()
        }
    }

    /**
     * Handles behaviour of what happens when the photo is clicked.
     * Sends the selected photo's id through intent and opens the PhotoDetailsActivity.
     *
     * // TODO Might need amending (moving to another class, companion object, etc.) but it needs to send the intent and start activity as specified.
     */
    private fun onPhotoClicked(){
        val selectedPhotoId = 1 //TODO Change 1 into the actual id of a selected photo
        val intent = Intent(this, PhotoDetailsActivity::class.java)
        intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, selectedPhotoId)
        startActivity(intent)
    }
}