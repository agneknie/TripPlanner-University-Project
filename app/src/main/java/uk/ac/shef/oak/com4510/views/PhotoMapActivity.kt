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

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import uk.ac.shef.oak.com4510.services.LocationService
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

    // The intent with which the service is called
    private lateinit var mLocationPendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActivity(this)
        setContext(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, interval
        ).build()

        configureLocationButton()

        // Gets all locations
        tripPlannerViewModel.photoLocations.observe(this){
            for (photoLocation in it){
                // Puts a location marker on the map
                putLocationMarkerOnMap(photoLocation)
            }
        }
    }

    /**
     * Implements location click functionality. When a location marker is clicked,
     * retrieves associated photo from the database and starts activity, which
     * displays its details.
     *
     * location: Location object, associated with the clicked location on the map.
     *
     * // TODO Use this when a location marker is clicked
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
        val markerTitle = location.dateTime.toString()
        markerLocationPairsList.add(Pair(markerTitle, location))

        // Plot a marker according to the given location
        // and move the camera to that marker
        val newMarker: Marker? = mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    location.xCoordinate,
                    location.yCoordinate
                )
            ).title("Time: ${location.dateTime}")
        )
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.xCoordinate,
                    location.yCoordinate
                ), 14.0f
            )
        )

    }

    /**
     * A necessary callback method that initialises the map
     * and plots every location with a photo to the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Setting a universal click listener for markers where
        // depending on the marker that is clicked its location is
        // retrieved and passed on to the locationClicked method in
        // order to redirect the user to the details of the photo
        // corresponding to that location.
        mMap.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            val location = retrieveLocation(it)
            locationClicked(location!!)
        })

    }

    /**
     * A helper function that takes as input a marker and depending
     * on its title returns the location corresponding to that marker.
     */
    private fun retrieveLocation(marker: Marker): Location? {
        val title = marker.title
        var location: Location? = null
        for (pair in markerLocationPairsList) {
            if (pair.first == title) location = pair.second
        }
        return location
    }

    override fun onResume() {
        super.onResume()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION),
                TripTripActivity.PERMISSION_LOCATION_GPS
            )

            return
        }
    }

    override fun onPause() {
        super.onPause()
    }

    private fun configureLocationButton(){
        val locFAB = binding.activityPhotoMapFabLocation
        locFAB.setOnClickListener{
            if (!locationFABState){
                locationFABState = true
                locFAB.setImageResource(R.drawable.ic_location_on)
                startLocationUpdates()
            } else {
                locationFABState = false
                locFAB.setImageResource(R.drawable.ic_location_off)
                stopLocationUpdates()
            }
        }
    }

    private fun setContext(context: Context) {
        mContext = context
    }

    /**
     * A function that initiates the location intent, checks if necessary permissions
     * have been granted and if so starts the location service.
     */
    private fun startLocationUpdates() {
        Log.e("LOCATION:", "Starting service...")

        val locationIntent = Intent(mContext, PhotoMapLocationService::class.java)
        mLocationPendingIntent =
            PendingIntent.getService(mContext,
                1,
                locationIntent,
                PendingIntent.FLAG_MUTABLE
            )

        Log.e("IntentService", "Getting...")



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION_GPS
            )

            return
        }

        val locationTask = mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationPendingIntent
        )
        locationTask.addOnFailureListener { e ->
            if (e is ApiException) {
                e.message?.let { Log.w("MapsActivity", it) }
            } else {
                Log.w("MapsActivity", e.message!!)
            }
        }
        locationTask.addOnCompleteListener{
            Log.d(
                "MapsActivity",
                "Successful GPS Start-Up!"
            )
        }
    }

    /**
     * A function that terminates location updates.
     */
    private fun stopLocationUpdates() {
        Log.e("LOC:", "Stopping Updates...")
        mFusedLocationProviderClient.removeLocationUpdates(mLocationPendingIntent)
    }

    /**
     * A companion object which contains necessary methods and parameters
     * needed for the location service.
     */
    companion object {
        val PERMISSION_LOCATION_GPS:Int = 1

        private var activity: AppCompatActivity? = null
        private lateinit var mMap: GoogleMap
        //private const val ACCESS_FINE_LOCATION = 123

        fun getActivity(): AppCompatActivity? {
            return activity
        }

        fun setActivity(newActivity: AppCompatActivity) {
            activity = newActivity
        }

        fun getMap(): GoogleMap {
            return mMap
        }

    }

}