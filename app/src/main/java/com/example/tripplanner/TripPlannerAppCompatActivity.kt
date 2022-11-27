package com.example.tripplanner

import android.view.View
import com.example.tripplanner.viewmodels.TripPlannerViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.viewmodels.TripPlannerViewModelFactory
import com.google.android.material.snackbar.Snackbar

/**
 * Instantiates ViewModels of the application.
 */
open class TripPlannerAppCompatActivity: AppCompatActivity() {

    // Initialise ViewModel
    protected val tripPlannerViewModel: TripPlannerViewModel by viewModels {
        TripPlannerViewModelFactory(
            (application as TripPlannerApplication).tripRepository,
            (application as TripPlannerApplication).locationRepository,
            (application as TripPlannerApplication).photoRepository,
            (application as TripPlannerApplication).tagRepository,
            application)
    }

    /**
     * Convenience method for displaying a Snackbar.
     */
    fun displaySnackbar(view: View, messageStringId: Int, displayLength: Int){
        val snackbar = Snackbar.make(view, messageStringId, displayLength)

        snackbar.setBackgroundTint(this.getColor(R.color.main_colour))
        snackbar.setTextColor(this.getColor(R.color.app_background))

        snackbar.show()
    }
}