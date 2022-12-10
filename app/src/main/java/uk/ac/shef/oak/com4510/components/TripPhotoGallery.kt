package uk.ac.shef.oak.com4510.components

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.components.adapters.TripPhotoAdapter
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import uk.ac.shef.oak.com4510.views.PhotoDetailsActivity

class TripPhotoGallery (
    private val invokingActivity: uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity,
    private val tripPlannerViewModel: TripPlannerViewModel,
    private val tripId: Int)
    : TripPhotoAdapter.TripPhotoItemClickListener {

    // Tag related variables
    private lateinit var tripPhotoRecyclerView: RecyclerView
    private lateinit var tripPhotoAdapter: TripPhotoAdapter

    init {
        setupTripPhotoRecyclerView()
    }

    /**
     * Photo RecyclerView setup. Observes Photo data in the database and
     * displays in the RecyclerView.
     */
    private fun setupTripPhotoRecyclerView(){
        val NUMBER_OF_COLUMNS = 3

        tripPhotoRecyclerView = invokingActivity.findViewById(R.id.activity_trip_overview_rv_photo_list)
        tripPhotoAdapter = TripPhotoAdapter(this)
        tripPhotoRecyclerView.adapter = tripPhotoAdapter
        tripPhotoRecyclerView.layoutManager = GridLayoutManager(invokingActivity, NUMBER_OF_COLUMNS)

        tripPlannerViewModel.getPhotosByTripId(tripId).observe(invokingActivity){
            it?.let{
                tripPhotoAdapter.submitList(it)
            }
        }
    }

    /**
     * Handles behaviour, which happens when a Photo is clicked.
     * Launches the PhotoDetailsActivity with selected Photo's id in the intent.
     */
    override fun onTripPhotoItemClick(photo: Photo, photoView: View) {
        val intent = Intent(invokingActivity, PhotoDetailsActivity::class.java)
        intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, photo.photoId)
        invokingActivity.startActivity(intent)
    }
}