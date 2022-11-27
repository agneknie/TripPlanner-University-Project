package com.example.tripplanner.views

import android.content.Intent
import android.os.Bundle
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.components.TagsPanel
import com.example.tripplanner.databinding.ActivityTripCreationBinding
import com.example.tripplanner.models.Trip
import com.example.tripplanner.utilities.IntentKeys
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime

class TripCreationActivity: TripPlannerAppCompatActivity(){
    private lateinit var binding: ActivityTripCreationBinding

    private lateinit var tagsPanel: TagsPanel

    override fun onCreate(savedInstanceState: Bundle?){
        // Activity binding & layout configuration
        super.onCreate(savedInstanceState)
        binding = ActivityTripCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tag Panel configuration
        tagsPanel = TagsPanel(this, binding, tripPlannerViewModel)

        // Listener for 'Start Trip' button
        configureStartTripButton()
    }

    /**
     * Configures "Start Trip" button behaviour. When trip is started, saves it in
     * the database, starts TripTripActivity and finishes this activity.
     */
    private fun configureStartTripButton(){
        binding.activityTripCreationBtnStartTrip.setOnClickListener{

            // Checks if trip title is present & saves it in database if so
            if(checkIfValidAndSaveTrip()){
                tripPlannerViewModel.currentTripId.observe(this){
                    it?.let{
                        // Saves current trip's id in the intent forwarded to TripTripActivity
                        val currentTripId = it
                        val intent = Intent(this, TripTripActivity::class.java)
                        intent.putExtra(IntentKeys.CURRENT_TRIP_ID, currentTripId)

                        // Starts TripTripActivity and finishes this one, so you cannot come back to it
                        startActivity(intent)
                        this.finish()
                    }
                }
            }
        }
    }

    /**
     * If trip information is supplied, saves trip in database.
     * If not, displays snackbar to the user
     */
    private fun checkIfValidAndSaveTrip(): Boolean {
        val tripCanStart = !binding.activityTripCreationEtTitle.text.isNullOrBlank()

        // Insert trip in the database
        if(tripCanStart)
            saveTripInDatabase()

        // Display error message
        else
           this.displaySnackbar(binding.root, R.string.trip_cannot_start_snackbar, Snackbar.LENGTH_LONG)

        return tripCanStart
    }

    /**
     * Saves trip in database with information supplied
     * from the interface.
     */
    private fun saveTripInDatabase(){
        val tripTitle = binding.activityTripCreationEtTitle.text.toString()
        val tripTag = tagsPanel.getSelectedTag()?.tagId
        val startTime = LocalDateTime.now()

        tripPlannerViewModel.insertTrip(Trip(0, startTime, null, tripTitle, tripTag))
    }
}