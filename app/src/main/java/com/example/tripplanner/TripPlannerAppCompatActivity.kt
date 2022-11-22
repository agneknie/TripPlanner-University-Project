package com.example.tripplanner

import com.example.tripplanner.viewmodels.TripPlannerViewModel
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tripplanner.viewmodels.TripPlannerViewModelFactory

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
}