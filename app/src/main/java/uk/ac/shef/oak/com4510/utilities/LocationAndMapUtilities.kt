package uk.ac.shef.oak.com4510.utilities

import android.graphics.Color
import uk.ac.shef.oak.com4510.services.LocationService

/**
 * Class LocationAndMapUtilities.
 *
 *  Stores variables, related to location, map and sensor services.
 */
class LocationAndMapUtilities {
    companion object{
        // Tracks sensor data every 20 secs as per assignment brief
        const val TRACKING_INTERVAL: Long = 20000

        // Used for location variation stabilisation
        const val LOCATION_CHANGE_THRESHOLD = 20

        // Default map zoom
        const val MAP_ZOOM = 17.0f

        // Map line between points default width
        const val MAP_LINE_WIDTH = 5F

        // Map line between points default colour
        const val MAP_LINE_COLOUR = Color.RED

        // Tag for the location service
        var LOCATION_SERVICE_TAG = LocationService::class.java.simpleName

    }
}