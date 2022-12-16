package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.data.access.database.DatabaseSeed
import uk.ac.shef.oak.com4510.databinding.ActivityMainBinding
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.models.Tag
import uk.ac.shef.oak.com4510.models.Trip
import uk.ac.shef.oak.com4510.utilities.Permissions
import uk.ac.shef.oak.com4510.utilities.PhotoUtilities
import java.io.File
import java.time.LocalDateTime

/**
 * Class MainActivity.
 *
 * Starting point of the application, which handles navigation to
 * the application's features.
 */
class MainActivity : TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Displays snackbar, if trip has finished in TripTripActivity
    private val tripTripActivityResultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        it?.let {
            if (it.resultCode == RESULT_OK){
                this.displaySnackbar(binding.root, R.string.trip_finished_successfully_snackbar)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialises button click listeners
        initialiseClickListeners()

        // Checks & requests permissions
        Permissions.checkAndRequestPermissions(this)

        //If database is empty, adds example trip
        tripPlannerViewModel.allTrips.observe(this){
            if(it.isEmpty())
                DatabaseSeed.seedExampleTrip(tripPlannerViewModel)
        }
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
           if(!Permissions.canAccessLocation(this)){
               // Informs the user
               displaySnackbar(binding.root, R.string.location_missing_snackbar)
           }
           else
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