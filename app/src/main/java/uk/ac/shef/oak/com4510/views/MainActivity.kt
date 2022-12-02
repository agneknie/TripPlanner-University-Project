package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityMainBinding
import uk.ac.shef.oak.com4510.utilities.Permissions

class MainActivity : TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Displays snackbar, if trip has finished in TripTripActivity
    private val tripTripActivityResultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        this.displaySnackbar(binding.root, R.string.trip_finished_successfully_snackbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialises button click listeners
        initialiseClickListeners()

        // Checks & requests permissions
        Permissions.checkAndRequestPermissions(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Permissions.onRequestPermissionsCallback(this, binding.root, requestCode)
    }

    /**
     * Initialises click listeners for New Trip, Photo Gallery,
     * Photo Map & Past Trips buttons.
     */
    private fun initialiseClickListeners(){
        // New Trip Button
       binding.activityMainBtnNewTrip.setOnClickListener {
            tripTripActivityResultContract.launch(Intent(this, TripCreationActivity::class.java))
        }

        // Photo Gallery Button
        binding.activityMainBtnPhotoGallery.setOnClickListener{
            startActivity(Intent(this, PhotoGalleryActivity::class.java))
        }

        // Photo Map Button
        binding.activityMainBtnPhotoMap.setOnClickListener{
            startActivity(Intent(this, PhotoMapActivity::class.java))
        }

        // Past Trips Button
        binding.activityMainBtnPastTrips.setOnClickListener{
            startActivity(Intent(this, TripGalleryActivity::class.java))
        }
    }

}