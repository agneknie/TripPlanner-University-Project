package uk.ac.shef.oak.com4510.views

import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.photo_details_panel.view.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.TagsPanel
import uk.ac.shef.oak.com4510.databinding.ActivityTripPhotoBinding
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.PhotoUtilities

class TripPhotoActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripPhotoBinding
    private lateinit var tagsPanel: TagsPanel

    private lateinit var photoPath: String
    private lateinit var thumbnailPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gets the selected or captured photo
        val bundle = intent.extras
        if(bundle != null){

            // Gets picked photo's uri
            photoPath = bundle.getString(IntentKeys.PHOTO_URI).toString()

            // Tag Panel configuration
            tagsPanel = TagsPanel(this, binding, tripPlannerViewModel)

            // Configure buttons
            configureCancelButton()
            configureAddToTripButton()

            // Creates thumbnail and saves its path
            thumbnailPath = PhotoUtilities.getOrMakeThumbNail(this, photoPath)

            //TODO Populates photo view
        }
        // If something unexpected happened, closes the activity and returns to trip screen
        else finish()
    }

    /**
     * Quits the activity without saving the photo and comes back to the trip screen.
     */
    private fun configureCancelButton(){
        binding.activityTripPhotoBtnCancel.setOnClickListener {
            finish()
        }
    }

    /**
     * Saves the photo to the database, together with its title, description & tags
     * if they are available.
     */
    private fun configureAddToTripButton(){
        binding.activityTripPhotoBtnAddToTrip.setOnClickListener {
            // If photo can be saved, saves it, informs the user and quits the activity.
            if(photoCanBeSaved()) {
                insertPhotoInDatabase()
                finish()
            }
            else
                displaySnackbar(binding.root, R.string.photo_needs_title_and_description)
        }
    }

    /**
     * Checks whether photo can be saved. A photo can be saved if it has a title and
     * a description.
     */
    private fun photoCanBeSaved(): Boolean{
        return !binding.root.photo_details_panel_ed_title.text.isNullOrBlank() &&
                !binding.root.photo_details_panel_ed_description.text.isNullOrBlank()
    }

    /**
     * Creates a photo with data available in the activity and saves it to the database.
     */
    private fun insertPhotoInDatabase(){
        tripPlannerViewModel.lastLocationId.observe(this){
            it?.let{
                val lastLocationId = it
                val photoTitle = binding.root.photo_details_panel_ed_title.text.toString()
                val photoDescription = binding.root.photo_details_panel_ed_description.text.toString()
                val tagId = tagsPanel.getSelectedTag()?.tagId

                tripPlannerViewModel.insertPhoto(
                    Photo(0, Uri.parse(photoPath), lastLocationId, photoTitle,
                        photoDescription, tagId, Uri.parse(thumbnailPath)))
            }
        }
    }
}