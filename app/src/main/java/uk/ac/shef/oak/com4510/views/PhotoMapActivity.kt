package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoMapBinding
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.utilities.IntentKeys

import android.app.PendingIntent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import uk.ac.shef.oak.com4510.R

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.Marker
import uk.ac.shef.oak.com4510.helpers.MapHelper
import uk.ac.shef.oak.com4510.services.PhotoMapLocationService

/**
 * Class PhotoMapActivity.
 *
 * Implements all photo viewing as locations on a map. When a
 * photo location is clicked, forwards to the activity, which displays
 * photo details.
 */
class PhotoMapActivity: TripPlannerAppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPhotoMapBinding
    private var locationFABState: Boolean = false
    // Necessary parameters for the location request
    private var interval : Long = 20000
    private lateinit var mContext: Context
    private val mapView: MapView? = null
    private var markerLocationPairsList = mutableListOf<Pair<String, Location>>()
    // The location request
    private lateinit var mLocationRequest: LocationRequest

    // The location provider
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    // The location intent(service)
    private lateinit var locationIntent: Intent

    // The intent with which the service is called
    private lateinit var mLocationPendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActivity(this)
        setContext(this)

        // Setting the location intent to the desired service
        locationIntent = Intent(mContext, PhotoMapLocationService::class.java)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, interval
        ).build()

        configureLocationButton()

        // TODO Fix floating button position
    }

    //region Button Configurations
    @Deprecated("Declaration overrides deprecated member but not marked as deprecated itself")
    override fun onBackPressed() {
        stopLocationUpdates()
        finish()
    }

    /**
     * Sets click listener for the current location button.
     */
    private fun configureLocationButton(){
        val locationButton = binding.activityPhotoMapFabLocation
        locationButton.setOnClickListener{
            if (!locationFABState){
                locationFABState = true
                locationButton.setImageResource(R.drawable.ic_location_on)
                startLocationUpdates()
            }
            else {
                locationFABState = false
                locationButton.setImageResource(R.drawable.ic_location_off)
                stopLocationUpdates()
            }
        }
    }
    //endregion

    //region Map Related methods
    /**
     * A necessary callback method that initialises the map
     * and plots every location with a photo to the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Gets all locations
        tripPlannerViewModel.photoLocations.observe(this){
            for (photoLocation in it){
                // Puts a location marker on the map
                putLocationMarkerOnMap(photoLocation)
            }
        }

        // Configuring marker click listener
        setMarkerClickListener(mMap)
    }

    /**
     * Implements location click functionality. When a location marker is clicked,
     * retrieves associated photo from the database and starts activity, which
     * displays its details.
     *
     * location: Location object, associated with the clicked location on the map.
     */
    private fun locationClicked(location: Location):Boolean{
        tripPlannerViewModel.getPhotoByLocation(location).observe(this){
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, it.photoId)
            startActivity(intent)
        }
        return false
    }

    /**
     * Given a Location object, puts a marker with that location on the map.
     */
    private fun putLocationMarkerOnMap(location: Location){
        val markerTitle = location.getLocationMarkerTitle()
        markerLocationPairsList.add(Pair(markerTitle, location))

        // Plot a marker according to the given location
        MapHelper.addLocationMarker(mMap, location, true)

        // Move the camera to that marker
        MapHelper.moveCameraToLocation(mMap, location)

    }

    /**
     * Setting a universal click listener for markers where
     * depending on the marker that is clicked its location is
     * retrieved and passed on to the locationClicked method in
     * order to redirect the user to the details of the photo
     * corresponding to that location.
     */
    private fun setMarkerClickListener(map: GoogleMap){
        map.setOnMarkerClickListener {
            val location = retrieveLocation(it)
            locationClicked(location!!)
        }
    }

    /**
     * A helper function that takes as input a marker and depending
     * on its title returns the location corresponding to that marker.
     */
    private fun retrieveLocation(marker: Marker): Location? {
        val markerTitle = marker.title
        var location: Location? = null

        // For each mapped photo location, searches for the clicked one
        for (pair in markerLocationPairsList) {
            val locationMarkerTitle = pair.second.getLocationMarkerTitle()

            // If the location is found, returns it
            if (markerTitle == locationMarkerTitle){
                location = pair.second
                break
            }
        }
        return location
    }
    //endregion

    //region Location Service related
    /**
     * A function that initiates the location intent, checks if necessary permissions
     * have been granted and if so starts the location service.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Log.e("LOCATION:", "Starting service...")

        mLocationPendingIntent =
            PendingIntent.getService(mContext,
                1,
                locationIntent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationPendingIntent)
    }

    /**
     * A function that terminates location updates.
     */
    private fun stopLocationUpdates() {
        if(this::mLocationPendingIntent.isInitialized)
            mFusedLocationProviderClient.removeLocationUpdates(mLocationPendingIntent)
        stopService(locationIntent)
        mMap.clear()
    }

    /**
     * Sets context for LocationService access.
     */
    private fun setContext(context: Context) {
        mContext = context
    }

    /**
     * A companion object which contains necessary methods and parameters
     * needed for the location service.
     */
    companion object {
        private var activity: AppCompatActivity? = null
        private lateinit var mMap: GoogleMap

        /**
         * Activity access for location service.
         */
        fun getActivity(): AppCompatActivity? {
            return activity
        }

        /**
         * Activity creation for location service.
         */
        fun setActivity(newActivity: AppCompatActivity) {
            activity = newActivity
        }

        /**
         * Map access for location service.
         */
        fun getMap(): GoogleMap {
            return mMap
        }
    }
    //endregion
}