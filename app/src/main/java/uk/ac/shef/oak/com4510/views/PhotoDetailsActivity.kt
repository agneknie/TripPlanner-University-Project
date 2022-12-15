package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.photo_details_panel.view.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.TagsPanel
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoDetailsBinding
import uk.ac.shef.oak.com4510.helpers.MapHelper
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.ServicesUtilities

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

    //region Button Configurations
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
    //endregion

    //region Activity Configuration
    /**
     * Populates the activity with photo details, its associated location details
     * and the map, which highlights the photo in its associated trip.
     */
    private fun configureActivity(){
        // Tag Panel configuration
        tagsPanel = TagsPanel(this, binding, tripPlannerViewModel)

        val bundle = intent.extras

        // Gets the selected photo id and configures activity with its data
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
    //endregion

    //region Map Related Methods
    /**
     * Callback method to initialise the map before plotting markers for
     * locations.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        configureActivity()
    }

    /**
     * Configures photo display by populating associated map view.
     */
    private fun configureMapDisplay(photoLocation: Location){
        // Variable to store each location in order to be able to
        // draw a line between it and the next one.
        var lastLocation: Location? = null

        // Get associated trip's locations and add them on the map
        tripPlannerViewModel.getLocationsByTrip(photoLocation.tripId).observe(this){
            it?.let{
                for(tripLocation in it){
                    // If current location is equal to photo location, mark it differently
                    if(photoLocation == tripLocation){
                        // Add location marker for a trip location
                        MapHelper.addLocationMarker(mMap, tripLocation, true)
                    }

                    // If current location is just a location, map it on the map
                    else{
                        MapHelper.addLocationMarker(mMap, tripLocation, false)

                        // Move camera to location
                        MapHelper.moveCameraToLocation(mMap, tripLocation)
                    }

                    // If locations have been initialised then draw lines between them
                    lastLocation?.let { MapHelper.drawLineBetweenLocations(mMap, it, tripLocation) }

                    lastLocation = tripLocation
                }
            }
        }
    }
    //endregion
}