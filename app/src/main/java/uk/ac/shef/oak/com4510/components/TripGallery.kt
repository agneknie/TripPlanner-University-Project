package uk.ac.shef.oak.com4510.components

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.models.Trip
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import uk.ac.shef.oak.com4510.views.TripOverviewActivity

class TripGallery(
    private val invokingActivity: uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity,
    private val tripPlannerViewModel: TripPlannerViewModel)
    : TripAdapter.TripItemClickListener {

    // Tag related variables
    private lateinit var tripRecyclerView: RecyclerView
    private lateinit var tripAdapter: TripAdapter

    init {
        setupTripRecyclerView()
    }

    /**
     * Trip RecyclerView setup. Observes Trip data in the database and
     * displays in the RecyclerView.
     */
    private fun setupTripRecyclerView(){
        val NUMBER_OF_COLUMNS = 1

        tripRecyclerView = invokingActivity.findViewById(R.id.activity_trip_gallery_rv_trip_list)
        tripAdapter = TripAdapter(this)
        tripRecyclerView.adapter = tripAdapter
        tripRecyclerView.layoutManager = StaggeredGridLayoutManager(NUMBER_OF_COLUMNS, StaggeredGridLayoutManager.VERTICAL)

        tripPlannerViewModel.allTrips.observe(invokingActivity){
            it?.let{
                tripAdapter.submitList(it)
            }
        }
    }

    /**
     * Handles behaviour, which happens when a Trip is clicked.
     * Launches the TripOverviewActivity with selected Trip's id in the intent.
     */
    override fun onTripItemClick(trip: Trip, tripView: View) {
        val intent = Intent(invokingActivity, TripOverviewActivity::class.java)
        intent.putExtra(IntentKeys.SELECTED_TRIP_ID, trip.tripId)
        invokingActivity.startActivity(intent)
    }
}