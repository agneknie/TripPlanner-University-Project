package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.TagsPanel
import uk.ac.shef.oak.com4510.components.TripPhotoGallery
import uk.ac.shef.oak.com4510.databinding.ActivityTripOverviewBinding
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Trip
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.ServicesUtilities

/**
 * Class TripOverviewActivity.
 *
 * When a Trip is selected from the Trip Gallery, displays the Trip's
 * data and allows further interaction with its elements.
 */
class TripOverviewActivity: TripPlannerAppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTripOverviewBinding
    private lateinit var mMap:GoogleMap
    private lateinit var tagsPanel: TagsPanel
    private var tripId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //region Button Configurations
    /**
     * Finishes the activity and updates trip details in the
     * database if they have changed.
     */
    private fun configureUpdateDetailsButton(){
        binding.activityTripOverviewBtnUpdateTrip.setOnClickListener {
            tripPlannerViewModel.getTrip(tripId).observe(this){
                val newTitle = binding.activityTripOverviewEtTripTitle.text.toString()
                val newTagId = tagsPanel.getSelectedTag()

                // Checks if title is not empty as it cannot be
                if(newTitle.isBlank())
                    displaySnackbar(binding.root, R.string.trip_cannot_start_snackbar)

                // If title is present, trip can be updated
                else{
                    it.title = newTitle

                    if(newTagId != null) it.tagId = newTagId.tagId
                    else it.tagId = null

                    tripPlannerViewModel.updateTrip(it)
                    finish()
                }
            }
        }
    }

    /**
     * Finishes the activity without updating trip details in
     * the database.
     */
    private fun configureGoBackButton(){
        binding.activityTripOverviewBtnGoBack.setOnClickListener {
            finish()
        }
    }
    //endregion

    //region Map related methods
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Gets selected trip id and configures activity with its data
        configureActivity()
    }

    /**
     * Recreates the trip's path from its locations in the map view.
     */
    private fun configureTripMap(trip: Trip){
        // Variable to store each location in order to be able to
        // draw a line between it and the next one.
        var lastLoc: Location? = null

        // Get associated locations and add them on the map
        tripPlannerViewModel.getLocationsByTrip(trip.tripId).observe(this){
            it?.let {
                for (tripLocation in it){

                    // Add location marker for a trip location
                    addLocationMarker(tripLocation)

                    // Move camera to location
                    moveCameraToLocation(tripLocation)

                    // If locations have been initialised then draw lines between them
                    lastLoc?.let { drawLineBetweenLocations(it, tripLocation) }

                    lastLoc = tripLocation
                }
            }
        }
    }

    /**
     * Adds location marker for the given location
     */
    private fun addLocationMarker(location: Location){
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(location.xCoordinate, location.yCoordinate))
                .title(location.getLocationMarkerTitle())
        )
    }

    /**
     * Moves map camera to the provided location.
     */
    private fun moveCameraToLocation(tripLocation: Location){
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(tripLocation.xCoordinate, tripLocation.yCoordinate),
                ServicesUtilities.MAP_ZOOM
            )
        )
    }

    /**
     * Draws a line/path on the map between two given locations.
     */
    private fun drawLineBetweenLocations(locationOne: Location, locationTwo: Location){
        mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(LatLng(locationOne.xCoordinate, locationOne.yCoordinate), LatLng(locationTwo.xCoordinate, locationTwo.yCoordinate))
                .width(ServicesUtilities.MAP_LINE_WIDTH)
                .color(ServicesUtilities.MAP_LINE_COLOUR)
                .geodesic(true) // to make the line curve
        )
    }
    //endregion

    //region Activity Fields Configuration
    /**
     * Populates the activity with trip details, its associated photos and
     * its associated locations on the map.
     */
    private fun configureActivity(){
        // Tag Panel configuration
        tagsPanel = TagsPanel(this, binding, tripPlannerViewModel)

        val bundle = intent.extras

        // Gets selected trip's id
        if(bundle != null){
            // Gets selected trip's id
            tripId = bundle.getInt(IntentKeys.SELECTED_TRIP_ID)

            // Gets selected trip from the database and populates the activity
            tripPlannerViewModel.getTrip(tripId).observe(this){
                it?.let{
                    // Configures trip details fields
                    configureTripDetails(it)

                    // Configures trip photo gallery
                    configureTripPhotoGallery(it)

                    // Configures trip location map
                    configureTripMap(it)
                }
            }
        }
        else finish()

        // Initialises 'Go Back' & 'Update Details' buttons
        configureGoBackButton()
        configureUpdateDetailsButton()
    }

    /**
     * Populates trip details fields with trip data.
     */
    private fun configureTripDetails(trip: Trip){
        // Configure title field
        binding.activityTripOverviewEtTripTitle.setText(trip.title)

        // Configure start time field
        binding.activityTripOverviewEtStartTime.setText(trip.getStartTimeAsString())

        // Configure end time field
        binding.activityTripOverviewEtEndTime.setText(trip.getEndTimeAsString())

        // Configure trip length field
        binding.activityTripOverviewEtTripLength.setText(trip.getLengthAsString())

        // Configures locations visited field
        configureLocationsVisitedField(trip.tripId)

        // Configures photos taken field
        configurePhotosTakenField(trip.tripId)

        // Configure tags if such exist
        tagsPanel.displayTagAsSelected(trip.tagId)
    }

    /**
     * Populates trip photo view with photos, which belong to the trip.
     */
    private fun configureTripPhotoGallery(trip: Trip){
        TripPhotoGallery(this, tripPlannerViewModel, trip.tripId)
    }

    /**
     * Configures 'Photos Taken' field.
     * Returns true, if there exist photos, taken during provided trip.
     */
    private fun configurePhotosTakenField(tripId: Int){
        tripPlannerViewModel.getPhotoCountByTrip(tripId).observe(this){
            var photoSuffix = "photo"
            if (it != 1) photoSuffix += "s"

            binding.activityTripOverviewEtPhotosTaken.setText("${it} ${photoSuffix}")
        }
    }

    /**
     * Configures 'Locations Visited' field.
     */
    private fun configureLocationsVisitedField(tripId: Int){
        tripPlannerViewModel.getLocationCountByTrip(tripId).observe(this){
            var locationSuffix = "location"
            if (it != 1) locationSuffix += "s"

            binding.activityTripOverviewEtLocationsVisited.setText("${it} ${locationSuffix}")
        }
    }
    //endregion
}