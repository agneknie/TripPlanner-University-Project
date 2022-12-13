package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoGalleryBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys

/**
 * Class PhotoGalleryActivity.
 *
 * Implements all photo browsing and sorting in a gallery of photos.
 */
class PhotoGalleryActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Remove. Placeholder so I can test the PhotoDisplay & PhotoDetails activities behaviour
        binding.activityPhotoGalleryIvSorting.setOnClickListener {
            photoClicked(1)
        }
    }

    /**
     * Handles behaviour of what happens when the photo is clicked. When a photo is clicked,
     * sends the selected photo's id through intent and opens the PhotoDetailsActivity.
     *
     * selectedPhoto: Photo object, associated with the clicked photo.
     *
     * // TODO Use this when a photo is clicked
     */
    private fun photoClicked(selectedPhotoId: Int){
        val intent = Intent(this, PhotoDetailsActivity::class.java)
        intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, selectedPhotoId)
        startActivity(intent)
    }
}