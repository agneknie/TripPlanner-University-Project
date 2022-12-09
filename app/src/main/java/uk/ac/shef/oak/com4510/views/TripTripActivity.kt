package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityTripTripBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.Permissions
import java.time.LocalDateTime

class TripTripActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripTripBinding

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

        // Configures camera & gallery button visibility based on permissions granted
        configureButtonVisibility()

        // Adds and configures all button listeners
        addAndConfigureButtonListeners()

        // TODO Insert map into activity_trip_trip_ll_map_holder. Set inserted map's height & width to match_parent.
    }

    @Deprecated("Declaration overrides deprecated member but not marked as deprecated itself")
    override fun onBackPressed() {
        this.displaySnackbar(binding.root, R.string.finish_trip_before_exiting_snackbar)
    }

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
}