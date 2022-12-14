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
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripTripBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.Permissions
import java.time.LocalDateTime

import android.annotation.SuppressLint
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import uk.ac.shef.oak.com4510.services.LocationService
import uk.ac.shef.oak.com4510.utilities.LocationAndMapUtilities

/**
 * Class TripTripActivity.
 *
 * Main Trip activity, where trip location tracking and photo selection or
 * taking happens.
 */
class TripTripActivity: TripPlannerAppCompatActivity(), OnMapReadyCallback  {
    private lateinit var binding: ActivityTripTripBinding

    //region Map related variables
    // Necessary parameters for the location request
    private lateinit var mapContext: Context
    private val mapView: MapView? = null
    // The location request
    private lateinit var mapLocationRequest: LocationRequest
    // The location provider
    private lateinit var mapFusedLocationProviderClient: FusedLocationProviderClient
    // The intent with which the service is called
    private lateinit var mapLocationPendingIntent: PendingIntent
    //endregion

    //region Callbacks for FAB buttons
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
        if(!photoUri.isNullOrBlank())
            launchTripPhotoActivity(photoUri)
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configures camera & gallery button visibility based on permissions granted
        configureButtonVisibility()

        // Adds and configures all button listeners
        addAndConfigureButtonListeners()

        // Configures Map & related services
        configureMapAndLocationService()

        // Starts updating the location
        startLocationUpdates()
    }

    //region Navigation related methods
    @Deprecated("Declaration overrides deprecated member but not marked as deprecated itself")
    override fun onBackPressed() {
        this.displaySnackbar(binding.root, R.string.finish_trip_before_exiting_snackbar)
    }

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
    //endregion

    //region Button Configurations & Listeners
    /**
     * Adds and configures listeners for 'Finish Trip', 'Gallery' &
     * 'Camera' buttons.
     */
    private fun addAndConfigureButtonListeners(){
        // Listener for "Finish Trip" button
        configureFinishTripButton()

        // Listener for "Gallery" button
        configureGalleryButton()

        // Listener for "Camera" button
        configureCameraButton()
    }

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
            // If something went wrong, finishes the activity
            else finish()
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

    //region Map Related Methods
    /**
     * Populates the map and starts location services.
     */
    private fun configureMapAndLocationService(){
        // Configures location service related variables
        setActivity(this)
        setContext(this)

        // Populates the map view in the activity
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activity_trip_trip_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configures and starts location & related services
        mapFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mapLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, LocationAndMapUtilities.TRACKING_INTERVAL
        ).build()
    }

    /**
     * Manipulates the map once available.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // TODO For the assignment, make this get the last recorder location from the trips database
        //  and initialise a marker on the map.

        // Move the camera to The Diamond if no location is available yet
        val diamond = LatLng(53.38190068158808, -1.4816251464270533)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(diamond))
    }

    /**
     * A helper function to set the activity's context for the location
     * service to properly plot the markers on the map.
     */
    private fun setContext(context: Context) {
        mapContext = context
    }

    /**
     * A function that initiates the location intent, checks if necessary permissions
     * have been granted and if so starts the location service.
     *
     * Suppressing permission check, as the activity will not be reachable if
     * location permission not given.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Creates the intent
        val locationIntent = Intent(mapContext, LocationService::class.java)
        mapLocationPendingIntent =
            PendingIntent.getService(mapContext,
                1,
                locationIntent,
                PendingIntent.FLAG_MUTABLE
            )

        // Starts location updates
        mapFusedLocationProviderClient.requestLocationUpdates(mapLocationRequest, mapLocationPendingIntent)
    }

    /**
     * Terminates location updates.
     */
    private fun stopLocationUpdates() {
        mapFusedLocationProviderClient.removeLocationUpdates(mapLocationPendingIntent)
    }

    /**
     * Companion object which containing methods and parameters
     * needed for the location service.
     */
    companion object {
        private var activity: AppCompatActivity? = null
        lateinit var mMap: GoogleMap

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
    //endregion
}