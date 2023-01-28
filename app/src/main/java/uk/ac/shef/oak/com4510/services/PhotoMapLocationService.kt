package uk.ac.shef.oak.com4510.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_PRESSURE
import android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.IBinder
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.helpers.MapHelper
import uk.ac.shef.oak.com4510.utilities.ServicesUtilities
import java.text.DateFormat
import java.util.*
import uk.ac.shef.oak.com4510.views.PhotoMapActivity

/**
 * Class PhotoMapLocationService.
 *
 * Handles user's location tracking in the PhotoMap.
 *
 * Uses Google Maps API.
 */
// TODO Refactor as LocationService.
class PhotoMapLocationService : Service {
    // Initialise variables for Sensor Access.
    private lateinit var sensorManager: SensorManager
    private var mPressureSensor: Sensor? = null
    private var mTemperatureSensor: Sensor? = null

    private var mPressureListener: SensorEventListener? = null
    private var mTemperatureListener: SensorEventListener? = null

    private var temperature: String? = null
    private var pressure: String? = null

    private var mCurrentLocation : Location? = null
    private var mLastLocation : Location? = null
    private var mLastUpdateTime : String? = null
    private var lastLocCircle: Circle? = null

    private var startMode : Int = 0
    private var binder : IBinder? = null
    private var allowRebind : Boolean = false

    constructor(name: String?) : super() {}
    constructor() : super() {}

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mPressureSensor = sensorManager.getDefaultSensor(TYPE_PRESSURE)
        mTemperatureSensor = sensorManager.getDefaultSensor(TYPE_AMBIENT_TEMPERATURE)

        mPressureListener = object : SensorEventListener {
            // Functions for when sensor value changes
            override fun onSensorChanged(event: SensorEvent) {
                pressure = event.values[0].toString()
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Not necessary in this implementation
            }
        }

        mTemperatureListener = object : SensorEventListener {
            // Functions for when sensor value changes
            override fun onSensorChanged(event: SensorEvent) {
                temperature = event.values[0].toString()
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Not necessary in this implementation
            }
        }

        if (mTemperatureSensor != null) {
            sensorManager.registerListener(mTemperatureListener, mTemperatureSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            temperature = ServicesUtilities.DEFAULT_SENSOR_VALUE
        }

        if (mPressureSensor != null) {
            sensorManager.registerListener(mPressureListener, mPressureSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            pressure = ServicesUtilities.DEFAULT_SENSOR_VALUE
        }

    }

    @SuppressLint("LongLogTag")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
//        Log.d(TAG, "Starting the foreground service...")
        //The service is starting
        if (LocationResult.hasResult(intent!!)) {
            val locResults = LocationResult.extractResult(intent)
            if (locResults != null) {
                for (location in locResults.locations) {
                    if (location == null) continue
                    mCurrentLocation = location
                    mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                    var msg = ""

                    if (PhotoMapActivity.getActivity() != null)
                        PhotoMapActivity.getActivity()?.runOnUiThread(Runnable {
                            try {
                                if (mLastLocation == null) {
                                    msg = "Initialised Location"
                                    lastLocCircle = PhotoMapActivity.getMap().addCircle(
                                        CircleOptions().center(
                                            LatLng(
                                                mCurrentLocation!!.latitude,
                                                mCurrentLocation!!.longitude
                                            )
                                        ).radius(10.0).fillColor(Color.CYAN).strokeColor(Color.CYAN)
                                    )
                                    PhotoMapActivity.getMap().moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(
                                                mCurrentLocation!!.latitude,
                                                mCurrentLocation!!.longitude
                                            ), 14.0f
                                        )
                                    )
                                    mLastLocation = mCurrentLocation
                                } else {
                                    if (mCurrentLocation != mLastLocation) {
                                        var distance = mLastLocation!!.distanceTo(mCurrentLocation!!)
                                        if (distance > 20) {
                                            msg ="Updated Location"
                                            lastLocCircle?.remove()
                                            lastLocCircle = PhotoMapActivity.getMap().addCircle(
                                                CircleOptions()
                                                    .center(
                                                        LatLng(
                                                            mCurrentLocation!!.latitude,
                                                            mCurrentLocation!!.longitude
                                                        ))
                                                    .radius(MapHelper.MAP_CURRENT_LOCATION_RADIUS)
                                                    .fillColor(MapHelper.MAP_CURRENT_LOCATION_COLOUR)
                                                    .strokeColor(MapHelper.MAP_CURRENT_LOCATION_COLOUR)
                                            )
                                            PhotoMapActivity.getMap().moveCamera(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(
                                                        mCurrentLocation!!.latitude,
                                                        mCurrentLocation!!.longitude
                                                    ), MapHelper.MAP_ZOOM
                                                )
                                            )

                                            mLastLocation = mCurrentLocation
                                        } else {
                                            msg = "Location hasn't changed."
                                        }
                                    }
                                }
                                // Informs the user
                                PhotoMapActivity.makeSnackbar(msg)
                            } catch (e: java.lang.Exception) {
                                PhotoMapActivity.makeSnackbar(R.string.cannot_write_map_snackbar)
                            }
                        })
                }
            }
        }
        return startMode
    }

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
        sensorManager.unregisterListener(mPressureListener)
        sensorManager.unregisterListener(mTemperatureListener)
    }

}