package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.photo_details_panel.view.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.TagsPanel
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoDetailsBinding
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.utilities.IntentKeys

/**
 * Class PhotoDetailsActivity.
 *
 * Displays photo thumbnail, photo's details and associated Location details.
 * Also, plots a map of the trip the photo belongs to.
 * Implements photo details updating in the database.
 */
class PhotoDetailsActivity: TripPlannerAppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPhotoDetailsBinding

    private lateinit var mMap: GoogleMap

    private lateinit var tagsPanel: TagsPanel
    private var photoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Callback method to initialise the map before plotting markers for
     * locations.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        configureActivity()
    }

    /**
     * Populates the activity with photo details, its associated location details
     * and the map, which highlights the photo in its associated trip.
     */
    private fun configureActivity(){
        // Tag Panel configuration
        tagsPanel = TagsPanel(this, binding, tripPlannerViewModel)

        // Gets the selected photo id and configures activity with its data
        val bundle = intent.extras!!

        // Gets selected photo's id
        if (bundle != null) {
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

            // Enables clicking on a photo to view it
            configurePhotoClick()
        }
        else finish()
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

                // Checks if required fields are valid
                if(newTitle.isBlank() || newDescription.isBlank())
                    displaySnackbar(binding.root, R.string.photo_needs_title_and_description)

                // If fields valid, updates photo details
                else{
                    it.title = newTitle
                    it.description = newDescription

                    if(newTagId != null) it.tagId = newTagId.tagId
                    else it.tagId = null

                    tripPlannerViewModel.updatePhoto(it)
                    finish()
                }
            }
        }
    }

    /**
     * Implements photo clicking functionality. When a photo is clicked,
     * new activity starts, which displays the full photo.
     */
    private fun configurePhotoClick(){
        binding.root.photo_details_panel_iv_photo.setOnClickListener {
            val intent = Intent(this, PhotoDisplayActivity::class.java)
            intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, photoId)
            startActivity(intent)
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
        // Get associated trip's locations and add them on the map
        tripPlannerViewModel.getLocationsByTrip(photoLocation.tripId).observe(this){
            it?.let{
                for(tripLocation in it){

                    mMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                tripLocation.xCoordinate,
                                tripLocation.yCoordinate
                            )
                        ).title(tripLocation.dateTime.toString())
                    )
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                tripLocation.xCoordinate,
                                tripLocation.yCoordinate
                            ), 14.0f
                        )
                    )
                    // If current location is equal to photo location, mark it differently
                    // Not sure if this is needed...
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