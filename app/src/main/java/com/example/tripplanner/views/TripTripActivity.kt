package com.example.tripplanner.views

import android.os.Bundle
import android.view.View
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityTripTripBinding
import com.example.tripplanner.utilities.Permissions
import com.google.android.material.snackbar.Snackbar

class TripTripActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripTripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configures camera & gallery button visibility based on permissions granted
        configureButtonVisibility()

        // TODO Insert map into activity_trip_trip_ll_map_holder. Set inserted map's height & width to match_parent.
    }

    @Deprecated("Declaration overrides deprecated member but not marked as deprecated itself")
    override fun onBackPressed() {
        this.displaySnackbar(binding.root, R.string.finish_trip_before_exiting_snackbar, Snackbar.LENGTH_LONG)
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
            this.displaySnackbar(binding.root, R.string.storage_missing_snackbar, Snackbar.LENGTH_LONG)
        }

        // Hide camera button if camera not accessible
        if(!Permissions.canUseCamera(this)){
            binding.activityTripTripFabCamera.visibility = View.INVISIBLE
            this.displaySnackbar(binding.root, R.string.camera_missing_snackbar, Snackbar.LENGTH_LONG)
        }
    }
}