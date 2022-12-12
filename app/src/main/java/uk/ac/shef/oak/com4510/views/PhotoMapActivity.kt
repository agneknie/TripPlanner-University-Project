package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoMapBinding

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.google.android.gms.maps.model.MarkerOptions
import uk.ac.shef.oak.com4510.services.LocationService

class PhotoMapActivity: TripPlannerAppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPhotoMapBinding
    private var locationFABState: Boolean = false
    // Necessary parameters for the location request
    private var interval : Long = 20000
    private lateinit var mContext: Context
    private val mapView: MapView? = null

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

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, interval
        ).build()

        configureLocationButton()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        TripTripActivity.mMap = googleMap

        // Add a marker in Sydney and move the camera.
        // For the assignment, make this get the last recorder location from the trips database
        // and initialise a marker on the map.
        val sydney = LatLng(-34.0, 151.0)
        TripTripActivity.mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        TripTripActivity.mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
            } else {
                locationFABState = false
                locFAB.setImageResource(R.drawable.ic_location_off)
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

        val locationIntent = Intent(mContext, LocationService::class.java)
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
                TripTripActivity.PERMISSION_LOCATION_GPS
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