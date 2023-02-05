package uk.ac.shef.oak.com4510.views

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripTripBinding
import uk.ac.shef.oak.com4510.services.LocationService
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.Permissions
import uk.ac.shef.oak.com4510.utilities.ServicesUtilities
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import java.time.LocalDateTime

/**
 * Class TripTripActivity.
 *
 * Main Trip activity, where trip location tracking and photo selection or
 * taking happens.
 */
class TripTripActivity: TripPlannerAppCompatActivity(), OnMapReadyCallback  {
    private lateinit var binding: ActivityTripTripBinding
    private var currentTripId: Int = 0

    //region Map related variables
    // Necessary parameters for the location request
    private lateinit var mapContext: Context
    private val mapView: MapView? = null
    // The location request
    private lateinit var mapLocationRequest: LocationRequest
    // The location provider
    private lateinit var mapFusedLocationProviderClient: FusedLocationProviderClient

    // The location intent(service)
    private lateinit var locationIntent: Intent

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

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setups current trip's id
        setCurrentTripId()

        // Configures camera & gallery button visibility based on permissions granted
        configureButtonVisibility()

        // Adds and configures all button listeners
        addAndConfigureButtonListeners()

        // Populates the map view in the activity
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activity_trip_trip_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configures Map & related services
        configureMapAndLocationService()
    }

    override fun onResume() {
        super.onResume()

        // Starts updating the location
        startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap.clear()
    }

    override fun onRestart() {
        super.onRestart()

        // Setups current trip's id
        setCurrentTripId()

        // Configures camera & gallery button visibility based on permissions granted
        configureButtonVisibility()

        // Adds and configures all button listeners
        addAndConfigureButtonListeners()

        // Configures Map & related services
        configureMapAndLocationService()
    }
    //endregion

    //region Navigation related methods
    @Deprecated("Declaration overrides deprecated member but not marked as deprecated itself")
    override fun onBackPressed() {
        stopLocationUpdates()
        activity?.finishAndRemoveTask()
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

            // Gets current trip from the database
            tripPlannerViewModel.getTrip(currentTripId).observe(this){
                it?.let{
                    // Updates current trip in database with its end time
                    it.endDateTime = LocalDateTime.now()
                    tripPlannerViewModel.updateTrip(it)

                    // Informs main activity, that trip has finished successfully
                    val intent = Intent(this, MainActivity::class.java)
                    setResult(RESULT_OK, intent)
                }
            }
            activity?.finishAndRemoveTask()
        }
    }

    /**
     * Gets current Trip's id from the bundle passed in
     * the intent. Finishes the activity if something goes wrong.
     */
    private fun setCurrentTripId(){
        val bundle = intent.extras

        if(bundle != null){
            // Gets current trip's id
            currentTripId = bundle.getInt(IntentKeys.CURRENT_TRIP_ID)
        }

        // If something went wrong, finishes the activity
        else finish()
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

        // Setting the location intent to the desired service
        locationIntent = Intent(mapContext, LocationService::class.java)

        // Configures and starts location & related services
        mapFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mapLocationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, ServicesUtilities.TRACKING_INTERVAL
        ).build()
    }

    /**
     * Manipulates the map once available.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        // Move the camera to The Diamond if no location is available yet
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

        mapLocationPendingIntent =
            PendingIntent.getService(mapContext,
                1,
                locationIntent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        // Starts location updates
        mapFusedLocationProviderClient.requestLocationUpdates(mapLocationRequest, mapLocationPendingIntent)
    }

    /**
     * Terminates location updates.
     */
    private fun stopLocationUpdates() {
        mapFusedLocationProviderClient.removeLocationUpdates(mapLocationPendingIntent)

        stopService(locationIntent)

    }
    //endregion

    //region Location Service related
    /**
     * Companion object which containing methods and parameters
     * needed for the location service.
     */
    companion object {
        private var activity: TripPlannerAppCompatActivity? = null
        lateinit var mMap: GoogleMap

        /**
         * Activity access for location service.
         */
        fun getActivity(): TripPlannerAppCompatActivity? {
            return activity
        }

        /**
         * Activity creation for location service.
         */
        fun setActivity(newActivity: TripPlannerAppCompatActivity) {
            activity = newActivity
        }

        /**
         * Map access for location service.
         */
        fun getMap(): GoogleMap {
            return mMap
        }

        /**
         * ViewModel access for location service.
         */
        fun getViewModel(): TripPlannerViewModel{
            return activity!!.getViewModel()
        }

        /**
         * Gets current Trip's id in the database.
         */
        fun getCurrentTripId(): Int{
            return (activity!! as TripTripActivity).currentTripId
        }

        /**
         * Make a snackbar on the current activity.
         * Uses string resource id for message.
         */
        fun makeSnackbar(messageResourceId :Int){
            val thisActivity = (activity!! as TripTripActivity)
            return thisActivity.displaySnackbar(thisActivity.binding.root, messageResourceId)
        }

        /**
         * Make a snackbar on the current activity.
         * Uses string for message.
         */
        fun makeSnackbar(messageString :String){
            val thisActivity = (activity!! as TripTripActivity)
            return thisActivity.displaySnackbar(thisActivity.binding.root, messageString)
        }

    }
    //endregion
}