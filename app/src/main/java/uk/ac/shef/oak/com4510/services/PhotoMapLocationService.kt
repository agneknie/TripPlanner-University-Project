package uk.ac.shef.oak.com4510.services

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.helpers.MapHelper
import uk.ac.shef.oak.com4510.helpers.MapHelper.Companion.MAP_CURRENT_LOCATION_COLOUR
import uk.ac.shef.oak.com4510.helpers.MapHelper.Companion.MAP_CURRENT_LOCATION_RADIUS
import uk.ac.shef.oak.com4510.utilities.ServicesUtilities
import uk.ac.shef.oak.com4510.views.PhotoMapActivity
import java.time.LocalDateTime

/**
 * Class PhotoMapLocationService.
 *
 * Handles user's location tracking in the PhotoMap.
 *
 * Uses Google Maps API.
 */
class PhotoMapLocationService : Service {

    //region Location related variables
    // Location sensor variables
    private var mCurrentLocation : Location? = null
    private var mLastLocation : Location? = null
    private var mLastUpdateTime : String? = null
    private var lastLocationCircle: Circle? = null
    //endregion

    //region Binder Related Variables
    private var startMode : Int = 0
    private var binder : IBinder? = null
    private var allowRebind : Boolean = false
    //endregion

    //region Constructors
    // Necessary for Service Implementation
    constructor(name: String?) : super()
    constructor() : super()
    //endregion

    //region Location Marker Display & Drawing on Map
    /**
     * Adds a location marker for the current location (mCurrentLocation),
     * zooms camera to the newly added marker.
     */
    private fun addLocationMarkerAndZoomCamera(){
        // Adds a new location marker
        addCurrentLocationMarker()

        // Moves the camera to new location
        moveCameraToCurrentLocation()
    }

    /**
     * Removes the last location marker (if present) and adds a new location marker using
     * the mCurrentLocation variable.
     */
    private fun addCurrentLocationMarker(){
        // Remove previous marker if present
        lastLocationCircle?.remove()

        // Add current marker
        lastLocationCircle = PhotoMapActivity.getMap().addCircle(
            CircleOptions()
                .center(LatLng(
                    mCurrentLocation!!.latitude,
                    mCurrentLocation!!.longitude
                ))
                .radius(MAP_CURRENT_LOCATION_RADIUS)
                .fillColor(MAP_CURRENT_LOCATION_COLOUR)
                .strokeColor(MAP_CURRENT_LOCATION_COLOUR)
        )
    }

    /**
     * Moves camera and zooms to the location, defined by mCurrentLocation.
     */
    private fun moveCameraToCurrentLocation(){
        PhotoMapActivity.getMap().moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude),
                MapHelper.MAP_ZOOM))
    }
    //endregion

    //region Location Tracking & Handling
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // The service is starting
        if (LocationResult.hasResult(intent!!)) {
            val locationResults = LocationResult.extractResult(intent)
            if (locationResults != null) {

                // For each location
                for (location in locationResults.locations) {
                    if (location == null) continue

                    // Get current location and its time
                    mCurrentLocation = location
                    mLastUpdateTime = LocalDateTime.now().toString()

                    // If activity exists and its map is writable performs location plotting
                    if (PhotoMapActivity.getActivity() != null)
                        PhotoMapActivity.getActivity()?.runOnUiThread {
                            try {
                                // Define message to display to user if necessary
                                val message = getAndHandleLocation()
                                PhotoMapActivity.makeSnackbar(message)
                            } catch (e: java.lang.Exception) {
                                System.err.println(e.message)
                                PhotoMapActivity.makeSnackbar(R.string.cannot_write_map_snackbar)
                            }
                        }
                }
            }
        }
        return startMode
    }

    /**
     * Handles the location by deciding whether it's a first location,
     * new location or a significant change in location. Plots it on the map
     * as user's current location.
     */
    private fun getAndHandleLocation(): String{

        // Initialises location if it is the first one
        val message: String = if (mLastLocation == null)
            handleFirstLocation()

        // Updates current location with the new location if it isn't the previous one
        else if (mCurrentLocation != mLastLocation) {

            // If location change is significant, proceeds to update current location
            if (locationChangeSignificant())
                handleNewLocation()

            // If location change is insignificant, informs the user
            else getString(R.string.location_has_not_changed_snackbar)
        } else getString(R.string.location_handling_unsuccessful_snackbar)

        return message
    }

    /**
     * Returns true, if the location change from the previous location
     * is significant according to the LocationAndMapUtilities.LOCATION_CHANGE_THRESHOLD.
     */
    private fun locationChangeSignificant(): Boolean{
        // Calculates if the location change is significant
        val distance = mLastLocation!!.distanceTo(mCurrentLocation!!)

        return distance > ServicesUtilities.LOCATION_CHANGE_THRESHOLD
    }

    /**
     * When first location is tracked, takes care of its display.
     */
    private fun handleFirstLocation(): String{
        // Adds first location marker and zooms camera to it
        addLocationMarkerAndZoomCamera()

        // Updates location
        mLastLocation = mCurrentLocation

        return getString(R.string.initialised_location_snackbar)
    }

    /**
     * When a new but not a first location is tracked, takes care of its
     * display.
     */
    private fun handleNewLocation(): String{
        // Adds new location marker, zooms camera to it and draws line between points
        addLocationMarkerAndZoomCamera()

        mLastLocation = mCurrentLocation

        return getString(R.string.updated_location_snackbar)
    }
    //endregion

    // region Override Methods
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        return allowRebind
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
    }
    //endregion
}