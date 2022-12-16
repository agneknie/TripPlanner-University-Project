package uk.ac.shef.oak.com4510.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_PRESSURE
import android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.helpers.MapHelper
import uk.ac.shef.oak.com4510.utilities.ServicesUtilities
import uk.ac.shef.oak.com4510.views.TripTripActivity
import java.time.LocalDateTime

/**
 * Class LocationService.
 *
 * Handles user's location tracking, together with barometric and
 * temperature data in the TripTripActivity.
 *
 * Uses Google Maps API.
 */
class LocationService : Service {

    //region Location, Pressure & Temperature related variables
    // Initialise variables for Sensor Access.
    private lateinit var sensorManager: SensorManager

    // Pressure sensor variables
    private var mPressureSensor: Sensor? = null
    private var mPressureListener: SensorEventListener? = null
    private var pressure: String = ServicesUtilities.DEFAULT_SENSOR_VALUE

    // Temperature sensor variables
    private var mTemperatureSensor: Sensor? = null
    private var mTemperatureListener: SensorEventListener? = null
    private var temperature: String = ServicesUtilities.DEFAULT_SENSOR_VALUE

    // Location sensor variables
    private var mCurrentLocation : Location? = null
    private var mLastLocation : Location? = null
    private var mLastUpdateTime : String? = null
    //endregion

    //region Binder related variables
    private var startMode : Int = 0
    private var binder : IBinder? = null
    private var allowRebind : Boolean = false
    //endregion

    //region Constructors
    // Necessary for Service Implementation
    constructor(name: String?) : super() {}
    constructor() : super() {}
    //endregion

    override fun onCreate() {
        super.onCreate()

        // Initialises sensor manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Configures and checks pressure sensor
        configureAndInitialisePressureSensor()

        // Configures and checks temperature sensor
        configureAndInitialiseTemperatureSensor()
    }

    //region Temperature & Pressure Sensor Setup
    /**
     * Gets the temperature sensor, initialises it and checks whether it has
     * been configured correctly.
     */
    private fun configureAndInitialiseTemperatureSensor(){
        // Gets sensor for temperature
        mTemperatureSensor = sensorManager.getDefaultSensor(TYPE_AMBIENT_TEMPERATURE)

        // Configures temperature listener
        mTemperatureListener = object : SensorEventListener {
            // Functions for when sensor value changes
            override fun onSensorChanged(event: SensorEvent) {
                temperature = event.values[0].toString()
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Not necessary in this implementation
            }
        }

        // Checks if temperature sensor is configured
        if (mTemperatureSensor != null) {
            sensorManager.registerListener(mTemperatureListener, mTemperatureSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
        else {
            temperature = ServicesUtilities.DEFAULT_SENSOR_VALUE
            // Makes snackbar "Temperature Sensor not Available."
            TripTripActivity.makeSnackbar(R.string.temperature_not_available_snackbar)
        }
    }

    /**
     * Gets the pressure sensor, initialises it and checks whether it has
     * been configured correctly.
     */
    private fun configureAndInitialisePressureSensor(){
        // Gets sensor for pressure
        mPressureSensor = sensorManager.getDefaultSensor(TYPE_PRESSURE)

        // Configures pressure listener
        mPressureListener = object : SensorEventListener {
            // When pressure sensor value changes
            override fun onSensorChanged(event: SensorEvent) {
                pressure = event.values[0].toString()
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Not necessary in this implementation
            }
        }

        // Checks if pressure sensor is configured
        if (mPressureSensor != null) {
            sensorManager.registerListener(mPressureListener, mPressureSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
        else {
            pressure = ServicesUtilities.DEFAULT_SENSOR_VALUE
            // Makes snackbar "Pressure Sensor not Available."
            TripTripActivity.makeSnackbar(R.string.pressure_not_available_snackbar)
        }
    }
    //endregion

    //region Location Marker Display & Drawing on Map
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
     * Adds a new location marker using the mCurrentLocation variable.
     *
     * Sets the location marker title to show time, temperature and pressure at
     * the time of measuring location.
     */
    private fun addCurrentLocationMarker(){
        TripTripActivity.getMap().addMarker(MarkerOptions()
            .position(
                LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude))
            .title(
                "Time: $mLastUpdateTime,\nTemperature: $temperature,\nPressure: $pressure"))
    }

    /**
     * Moves camera and zooms to the location, defined by mCurrentLocation.
     */
    private fun moveCameraToCurrentLocation(){
        TripTripActivity.getMap().moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude),
                MapHelper.MAP_ZOOM))
    }

    /**
     * Adds a location marker for the current location (mCurrentLocation),
     * zooms camera to the newly added marker and draws a line between the
     * previous location and this one
     *
     * firstLocation- true, if first location on the map and line between this
     * and the previous location marker should not be drawn.
     */
    private fun addLocationMarkerAndZoomCamera(firstLocation: Boolean){
        // Adds a new location marker
        addCurrentLocationMarker()

        // Moves the camera to new location
        moveCameraToCurrentLocation()

        if(!firstLocation){
            // Adds a line between the last location and current one
            drawLineBetweenCurrentAndLastLocations()
        }
    }

    /**
     * Draws a PolyLine between the current and last location, defined by
     * mCurrentLocation and mLastLocation respectively.
     */
    private fun drawLineBetweenCurrentAndLastLocations(){
        TripTripActivity.getMap().addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude),
                    LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude))
                .width(MapHelper.MAP_LINE_WIDTH)
                .color(MapHelper.MAP_LINE_COLOUR)
                // Curved line
                .geodesic(true))
    }
    //endregion

    //region Location Tracking & Handling
    /**
     * Location, pressure and temperature tracking implementation.
     */
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

                    // If activity exists and its map is writable performs location plotting & saving
                    if (TripTripActivity.getActivity() != null)
                        TripTripActivity.getActivity()?.runOnUiThread {
                            try {
                                // Define message to display to user if necessary
                                val message = getAndHandleLocation()
                                TripTripActivity.makeSnackbar(message)
                            } catch (e: java.lang.Exception) {
                                TripTripActivity.makeSnackbar(R.string.cannot_write_map_snackbar)
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
     * and saves it in the service for later access and in the database.
     */
    private fun getAndHandleLocation(): String{
        val message: String

        // Initialises location if it is the first one
        if (mLastLocation == null)
            message = handleFirstLocation()

        // Updates current location with the new location if it isn't the previous one
        else if (mCurrentLocation != mLastLocation) {

            // If location change is significant, proceeds to update current location
            if (locationChangeSignificant())
                message = handleNewLocation()

            // If location change is insignificant, informs the user
            else message = getString(R.string.location_has_not_changed_snackbar)
        }

        else message = getString(R.string.location_handling_unsuccessful_snackbar)

        return message
    }

    /**
     * When first location is tracked, takes care of its display and
     * saving in the service and the database.
     */
    private fun handleFirstLocation(): String{
        // Adds first location marker and zooms camera to it
        addLocationMarkerAndZoomCamera(true)

        // Updates location
        mLastLocation = mCurrentLocation

        // Adds the location to the database
        saveLocationInDatabase()

        return getString(R.string.initialised_location_snackbar)
    }

    /**
     * When a new but not a first location is tracked, takes care of its
     * display and saving in the service and the database.
     */
    private fun handleNewLocation(): String{
        // Adds new location marker, zooms camera to it and draws line between points
        addLocationMarkerAndZoomCamera(false)

        mLastLocation = mCurrentLocation

        // Adds the location to the database
        saveLocationInDatabase()

        return getString(R.string.updated_location_snackbar)
    }

    /**
     * Adds the location, together with its temperature and pressure
     * to the database.
     */
    private fun saveLocationInDatabase(){
        val newLocation = uk.ac.shef.oak.com4510.models.Location(
            0,
            mCurrentLocation!!.latitude,
            mCurrentLocation!!.longitude,
            temperature,
            pressure,
            LocalDateTime.parse(mLastUpdateTime),
            TripTripActivity.getCurrentTripId())

        TripTripActivity.getViewModel().insertLocation(newLocation)
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

    override fun onDestroy() {
        super.onDestroy()
        Log.e("LocationService", "Destroying Service...")
        // Releases pressure and temperature listeners
        sensorManager.unregisterListener(mPressureListener)
        sensorManager.unregisterListener(mTemperatureListener)
    }
    //endregion
}