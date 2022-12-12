package uk.ac.shef.oak.com4510.services

import android.annotation.SuppressLint
import android.app.IntentService
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
import android.os.Handler
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.DateFormat
import java.util.*
import uk.ac.shef.oak.com4510.views.TripTripActivity

class LocationService : Service {

    // Initialise variables for Sensor Access.
    private lateinit var sensorManager: SensorManager
    private var mPressureSensor: Sensor? = null
    private var mTemperatureSensor: Sensor? = null

    private var mPressureListener: SensorEventListener? = null
    private var mTemperatureListener: SensorEventListener? = null

    private var temperature: String? = null
    private var pressure: String? = null

    companion object {
        var currentService: LocationService? = null

        // it is static so to make sure that it is always initialised when the viewmodel live data is
        // is created, otherwise you risk a disconnection
        var counter: MutableLiveData<Int> = MutableLiveData(0)
        private val TAG = LocationService::class.java.simpleName
        private const val NOTIFICATION_ID = 9974
    }

    private var mCurrentLocation : Location? = null
    private var mLastLocation : Location? = null
    private var mLastUpdateTime : String? = null

    private var startMode : Int = 0
    private var binder : IBinder? = null
    private var allowRebind : Boolean = false

    constructor(name: String?) : super() {}
    constructor() : super() {}

    override fun onCreate() {
        super.onCreate()
        Log.e("Location Service", "onCreate finished")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mPressureSensor = sensorManager.getDefaultSensor(TYPE_PRESSURE)
        mTemperatureSensor = sensorManager.getDefaultSensor(TYPE_AMBIENT_TEMPERATURE)

        mPressureListener = object : SensorEventListener {
            // Functions for when sensor value changes
            override fun onSensorChanged(event: SensorEvent) {
                pressure = event.values[0].toString()
                Log.e(TAG,"Current Pressure: $pressure")
//                val ls_min_delay = lightSensor?.minDelay.toString()
//                Log.i(TAG, "Minimum Light Sensor Delay: $ls_min_delay")
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Ignoring
            }
        }

        mTemperatureListener = object : SensorEventListener {
            // Functions for when sensor value changes
            override fun onSensorChanged(event: SensorEvent) {
                temperature = event.values[0].toString()

//                val ls_min_delay = lightSensor?.minDelay.toString()
//                Log.i(TAG, "Minimum Light Sensor Delay: $ls_min_delay")
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Ignoring
            }
        }

        if (mTemperatureSensor != null) {
            Log.e(TAG, "Successfully registered temperature listener...")
            sensorManager.registerListener(mTemperatureListener, mTemperatureSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Log.e(TAG, "This phone doesn't have a temperature sensor...")
            temperature = "UNAVAILABLE"
        }

        if (mPressureSensor != null) {
            Log.e(TAG, "Successfully registered barometer listener...")
            sensorManager.registerListener(mPressureListener, mPressureSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Log.e(TAG, "This phone doesn't have a pressure sensor...")
            pressure = "UNAVAILABLE"
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

                    Log.i(TAG, "Current Location: $location")
                    mCurrentLocation = location
                    mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                    var msg = ""
                    //Log.i("This is in service, MAP", "New Location:" + mCurrentLocation.toString())

                    if (TripTripActivity.getActivity() != null)
                        TripTripActivity.getActivity()?.runOnUiThread(Runnable {
                            try {
                                if (mLastLocation == null) {
                                    Log.i(TAG, "Location initialised: ${mCurrentLocation}.")
                                    msg = "Initialised Location"
                                    TripTripActivity.getMap().addMarker(
                                        MarkerOptions().position(
                                            LatLng(
                                                mCurrentLocation!!.latitude,
                                                mCurrentLocation!!.longitude
                                            )
                                        ).title("Time: $mLastUpdateTime,\nTemperature: $temperature,\nPressure: $pressure")
                                    )
                                    TripTripActivity.getMap().moveCamera(
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
                                            Log.i(TAG, "Location has changed: ${mCurrentLocation}.")
                                            msg ="Updated Location"
                                            TripTripActivity.getMap().addMarker(
                                                MarkerOptions().position(
                                                    LatLng(
                                                        mCurrentLocation!!.latitude,
                                                        mCurrentLocation!!.longitude
                                                    )
                                                ).title("Time: $mLastUpdateTime,\nTemperature: $temperature,\nPressure: $pressure")
                                            )
                                            TripTripActivity.getMap().moveCamera(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(
                                                        mCurrentLocation!!.latitude,
                                                        mCurrentLocation!!.longitude
                                                    ), 17.0f
                                                )
                                            )

                                            val polyline1 = TripTripActivity.getMap().addPolyline(
                                                PolylineOptions().clickable(true)
                                                    .add(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude), LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude))
                                                    .width(5F)
                                                    .color(Color.RED)
                                                    .geodesic(true) // to make the line curve
                                            )

                                            mLastLocation = mCurrentLocation
                                        } else {
                                            Log.i(TAG, "Location hasn't changed: ${mCurrentLocation}.")
                                            msg = "Location hasn't changed."
                                        }
                                    }
                                }
                                val duration = Toast.LENGTH_SHORT
                                var toast = Toast.makeText(applicationContext, msg, duration)
                                // to show the toast
                                toast.show();
                            } catch (e: java.lang.Exception) {
                                Log.e(TAG, "Error, cannot write on map " + e.message)
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
        Log.e("Service", "Ending Service...")
    }

}