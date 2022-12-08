package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.TagsPanel
import uk.ac.shef.oak.com4510.databinding.ActivityTripCreationBinding
import uk.ac.shef.oak.com4510.models.Trip
import uk.ac.shef.oak.com4510.utilities.IntentKeys
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

    @Deprecated("Declaration overrides deprecated member but not marked as deprecated itself")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        setResult(RESULT_CANCELED, intent)
        finish()
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
                        val intentToMain = Intent(this, MainActivity::class.java)
                        setResult(RESULT_OK, intentToMain)
                        finish()
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
           this.displaySnackbar(binding.root, R.string.trip_cannot_start_snackbar)

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