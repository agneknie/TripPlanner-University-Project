package uk.ac.shef.oak.com4510.utilities

/**
 * Class ServicesUtilities.
 *
 *  Stores variables, related to location, map and sensor services.
 */
class ServicesUtilities {
    companion object{
        // Tracks sensor data every 20 secs as per assignment brief
        const val TRACKING_INTERVAL: Long = 20000

        // Used for location variation stabilisation
        const val LOCATION_CHANGE_THRESHOLD = 20

        // Default value for sensors
        const val DEFAULT_SENSOR_VALUE = "Unavailable"
    }
}