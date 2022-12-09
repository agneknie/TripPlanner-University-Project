package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import kotlinx.android.synthetic.main.photo_details_panel.view.*
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.TagsPanel
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoDetailsBinding
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.utilities.IntentKeys

class PhotoDetailsActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoDetailsBinding

    private lateinit var tagsPanel: TagsPanel
    private var photoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gets the selected photo id and configures activity with its data
        val bundle = intent.extras
        if(bundle != null) configureActivity(bundle)

        // If something went wrong, closes the activity
        else finish()
    }

    /**
     * Populates the activity with photo details, its associated location details
     * and the map, which highlights the photo in its associated trip.
     */
    private fun configureActivity(bundle: Bundle){
        // Tag Panel configuration
        tagsPanel = TagsPanel(this, binding, tripPlannerViewModel)

        // Gets selected photo's id
        photoId = bundle.getInt(IntentKeys.SELECTED_PHOTO_ID)

        // Gets selected photo from the database and populates the activity
        tripPlannerViewModel.getPhoto(photoId).observe(this){
            it?.let{
                // Configures photo details related fields
                configurePhotoDisplayAndDetails(it)

                // Configures location details related fields & map
                configurePhotoLocationDetailsAndMap(it)
            }
        }

        // Initialises 'Go Back' & 'Update Details' buttons
        configureGoBackButton()
        configureUpdateDetailsButton()
    }

    /**
     * Finishes the activity without updating photo details in
     * the database.
     */
    private fun configureGoBackButton(){
        binding.activityPhotoDetailsBtnGoBack.setOnClickListener {
            finish()
        }
    }

    /**
     * Finishes the activity and updates photo details in the
     * database if they have changed.
     */
    private fun configureUpdateDetailsButton(){
        binding.activityPhotoDetailsBtnUpdatePhoto.setOnClickListener {
            tripPlannerViewModel.getPhoto(photoId).observe(this){
                val newTitle = binding.root.photo_details_panel_ed_title.text.toString()
                val newDescription = binding.root.photo_details_panel_ed_description.text.toString()
                val newTagId = tagsPanel.getSelectedTag()

                it.title = newTitle
                it.description = newDescription

                if(newTagId != null) it.tagId = newTagId.tagId
                else it.tagId = null

                tripPlannerViewModel.updatePhoto(it)

                finish()
            }
        }
    }

    /**
     * Configures photo display by populating the image view with the
     * photo's thumbnail and adding photo's title, description and tags.
     */
    private fun configurePhotoDisplayAndDetails(photo: Photo){
        // Populates photo's image view
        binding.root.photo_details_panel_iv_photo.setImageURI(photo.thumbnailPath)

        // Configure title field
        binding.root.photo_details_panel_ed_title.setText(photo.title)

        // Configure description field
        binding.root.photo_details_panel_ed_description.setText(photo.description)

        // Configure tags if such exist
        tagsPanel.displayTagAsSelected(photo.tagId)
    }

    /**
     * Configures photo display by populating associated photo location data.
     */
    private fun configurePhotoLocationDetailsAndMap(photo: Photo){
        tripPlannerViewModel.getLocation(photo.locationId).observe(this){
            it?.let{
                // Configures map element of the photo
                configureMapDisplay(it)

                // Configure temperature field
                binding.activityPhotoDetailsEdTemperature.setText(it.temperature.toString())

                // Configure pressure field
                binding.activityPhotoDetailsEdPressure.setText(it.pressure.toString())
            }
        }
    }

    /**
     * Configures photo display by populating associated map view.
     */
    private fun configureMapDisplay(photoLocation: Location){
        // Initialise map display
        // TODO Insert map into activity_photo_details_ll_map_holder. Amend height as necessary

        // Get associated trip's locations and add them on the map
        tripPlannerViewModel.getLocationsByTrip(photoLocation.tripId).observe(this){
            it?.let{
                for(tripLocation in it){
                    // If current location is equal to photo location, mark it differently
                    if(photoLocation == tripLocation){
                        // TODO Maps photo location on the map
                    }

                    // If current location is just a location, map it on the map
                    else{
                        // TODO Maps trip location on the map
                    }
                }
            }
        }
    }
}