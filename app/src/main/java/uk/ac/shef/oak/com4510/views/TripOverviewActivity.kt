package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.TagsPanel
import uk.ac.shef.oak.com4510.components.TripPhotoGallery
import uk.ac.shef.oak.com4510.databinding.ActivityTripOverviewBinding
import uk.ac.shef.oak.com4510.models.Trip
import uk.ac.shef.oak.com4510.utilities.IntentKeys

/**
 * Class TripOverviewActivity.
 *
 * When a Trip is selected from the Trip Gallery, displays the Trip's
 * data and allows further interaction with its elements.
 */
class TripOverviewActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityTripOverviewBinding

    private lateinit var tagsPanel: TagsPanel
    private var tripId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gets selected trip id and configures activity with its data
        val bundle = intent.extras
        if(bundle != null) configureActivity(bundle)

        // If something went wrong, closes the activity
        else finish()
    }

    /**
     * Populates the activity with trip details, its associated photos and
     * its associated locations on the map.
     */
    private fun configureActivity(bundle: Bundle){
        // Tag Panel configuration
        tagsPanel = TagsPanel(this, binding, tripPlannerViewModel)

        // Gets selected trip's id
        tripId = bundle.getInt(IntentKeys.SELECTED_TRIP_ID)

        // Gets selected trip from the database and populates the activity
        tripPlannerViewModel.getTrip(tripId).observe(this){
            it?.let{
                // Configures trip details fields
                configureTripDetails(it)

                // Configures trip photo gallery
                configureTripPhotoGallery(it)

                // Configures trip location map
                configureTripMap(it)
            }
        }

        // Initialises 'Go Back' & 'Update Details' buttons
        configureGoBackButton()
        configureUpdateDetailsButton()
    }

    /**
     * Finishes the activity without updating trip details in
     * the database.
     */
    private fun configureGoBackButton(){
        binding.activityTripOverviewBtnGoBack.setOnClickListener {
            finish()
        }
    }

    /**
     * Finishes the activity and updates trip details in the
     * database if they have changed.
     */
    private fun configureUpdateDetailsButton(){
        binding.activityTripOverviewBtnUpdateTrip.setOnClickListener {
            tripPlannerViewModel.getTrip(tripId).observe(this){
                val newTitle = binding.activityTripOverviewEtTripTitle.text.toString()
                val newTagId = tagsPanel.getSelectedTag()

                // Checks if title is not empty as it cannot be
                if(newTitle.isBlank())
                    displaySnackbar(binding.root, R.string.trip_cannot_start_snackbar)

                // If title is present, trip can be updated
                else{
                    it.title = newTitle

                    if(newTagId != null) it.tagId = newTagId.tagId
                    else it.tagId = null

                    tripPlannerViewModel.updateTrip(it)
                    finish()
                }
            }
        }
    }

    /**
     * Populates trip details fields with trip data.
     */
    private fun configureTripDetails(trip: Trip){
        // Configure title field
        binding.activityTripOverviewEtTripTitle.setText(trip.title)

        // Configure start time field
        binding.activityTripOverviewEtStartTime.setText(trip.getStartTimeAsString())

        // Configure end time field
        binding.activityTripOverviewEtEndTime.setText(trip.getEndTimeAsString())

        // Configure trip length field
        binding.activityTripOverviewEtTripLength.setText(trip.getLengthAsString())

        // Configures locations visited field
        configureLocationsVisitedField(trip.tripId)

        // Configures photos taken field
        configurePhotosTakenField(trip.tripId)

        // Configure tags if such exist
        tagsPanel.displayTagAsSelected(trip.tagId)
    }

    /**
     * Populates trip photo view with photos, which belong to the trip.
     */
    private fun configureTripPhotoGallery(trip: Trip){
        TripPhotoGallery(this, tripPlannerViewModel, trip.tripId)
    }

    /**
     * Recreates the trip's path from its locations in the map view.
     */
    private fun configureTripMap(trip: Trip){
        // Initialise map display
        // TODO Insert map into activity_trip_overview_ll_map_holder. Amend height as necessary

        // Get associated locations and add them on the map
        tripPlannerViewModel.getLocationsByTrip(trip.tripId).observe(this){
            it?.let {
                for (tripLocation in it){
                    // TODO Adds location marker on the map
                }
            }
        }
    }

    /**
     * Configures 'Photos Taken' field.
     * Returns true, if there exist photos, taken during provided trip.
     */
    private fun configurePhotosTakenField(tripId: Int){
        tripPlannerViewModel.getPhotoCountByTrip(tripId).observe(this){
            var photoSuffix = "photo"
            if (it != 1) photoSuffix += "s"

            binding.activityTripOverviewEtPhotosTaken.setText("${it} ${photoSuffix}")
        }
    }

    /**
     * Configures 'Locations Visited' field.
     */
    private fun configureLocationsVisitedField(tripId: Int){
        tripPlannerViewModel.getLocationCountByTrip(tripId).observe(this){
            var locationSuffix = "location"
            if (it != 1) locationSuffix += "s"

            binding.activityTripOverviewEtLocationsVisited.setText("${it} ${locationSuffix}")
        }
    }
}