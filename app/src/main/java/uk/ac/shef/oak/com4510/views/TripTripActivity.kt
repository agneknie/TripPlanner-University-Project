package uk.ac.shef.oak.com4510.views

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripTripBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.Permissions
import java.time.LocalDateTime

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import uk.ac.shef.oak.com4510.services.LocationService

class TripTripActivity: TripPlannerAppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTripTripBinding

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



    // When user has picked out a photo, forwards them to TripPhotoActivity with picked photo's uri
    private val photoPickerActivityResultContract = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        it?.let {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            this.contentResolver.takePersistableUriPermission(it, flag)

            launchTripPhotoActivity(it.toString())
        }
    }

    // When user took a picture, forwards them to TripPhotoActivity with picked photo's uri
    private val cameraActivityResultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val photoUri = it.data?.extras?.getString(IntentKeys.PHOTO_URI)

        launchTripPhotoActivity(photoUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        setActivity(this)
        setContext(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, interval
        ).build()

        // Configures camera & gallery button visibility based on permissions granted
        configureButtonVisibility()

        // Listener for "Finish Trip" button
        configureFinishTripButton()

        // Listener for "Gallery" button
        configureGalleryButton()

        // Listener for "Camera" button
        configureCameraButton()

        // TODO Insert map into activity_trip_trip_ll_map_holder. Set inserted map's height & width to match_parent.
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
                PERMISSION_LOCATION_GPS)

            return
        }
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera.
        // For the assignment, make this get the last recorder location from the trips database
        // and initialise a marker on the map.
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }



    @Deprecated("Declaration overrides deprecated member but not marked as deprecated itself")
    override fun onBackPressed() {
        this.displaySnackbar(binding.root, R.string.finish_trip_before_exiting_snackbar)
    }

    //region Button Configurations & Listeners
    /**
     * Configures camera & gallery button visibility based on
     * whether permissions are granted and if user has access to required
     * features.
     *
     * If not accessible for any reason, displays information snackbar.
     */
    private fun configureButtonVisibility(){
        // Hide gallery button if gallery not accessible
        if(!Permissions.canModifyStorage(this)){
            binding.activityTripTripFabGallery.visibility = View.INVISIBLE
            this.displaySnackbar(binding.root, R.string.storage_missing_snackbar)
        }

        // Hide camera button if camera not accessible
        if(!Permissions.canUseCamera(this)){
            binding.activityTripTripFabCamera.visibility = View.INVISIBLE
            this.displaySnackbar(binding.root, R.string.camera_missing_snackbar)
        }
    }

    /**
     * If "Finish Trip" button is clicked, updates the trip in the database
     * with its finish time.
     */
    private fun configureFinishTripButton(){
        binding.activityTripTripBtnFinishTrip.setOnClickListener{
            // Stopping location updates
            stopLocationUpdates()

            val bundle = intent.extras
            if(bundle != null){
                // Gets current trip's id
                val tripId = bundle.getInt(IntentKeys.CURRENT_TRIP_ID)

                // Gets current trip from the database
                tripPlannerViewModel.getTrip(tripId).observe(this){
                    it?.let{
                        // Updates current trip in database with its end time
                        it.endDateTime = LocalDateTime.now()
                        tripPlannerViewModel.updateTrip(it)

                        // Informs main activity, that trip has finished successfully
                        val intent = Intent(this, MainActivity::class.java)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }

    /**
     * If Gallery FAB is clicked, opens gallery view.
     */
    private fun configureGalleryButton(){
        binding.activityTripTripFabGallery.setOnClickListener{
            photoPickerActivityResultContract.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    /**
     * If Camera FAB is clicked, opens camera view.
     */
    private fun configureCameraButton(){
        binding.activityTripTripFabCamera.setOnClickListener {
            cameraActivityResultContract.launch(Intent(this, CameraActivity::class.java))
        }
    }

    //endregion

    /**
     * Launches TripPhotoActivity, creating an intent, which contains uri of
     * a photo, which was either picked from the gallery or took with the camera.
     */
    private fun launchTripPhotoActivity(photoUriString: String?){
        // Launch TripPhotoActivity
        val intent = Intent(this, TripPhotoActivity::class.java)
        intent.putExtra(IntentKeys.PHOTO_URI, photoUriString)
        startActivity(intent)
    }

    /**
     * A helper function to set the activity's context.
     * This is needed for the location service to properly
     * plot the markers on the map.
     */
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
                PERMISSION_LOCATION_GPS)

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
        lateinit var mMap: GoogleMap
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