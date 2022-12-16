package uk.ac.shef.oak.com4510.components

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.components.adapters.GalleryPhotoAdapter
import uk.ac.shef.oak.com4510.helpers.PhotoSortingOption
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import uk.ac.shef.oak.com4510.views.PhotoDetailsActivity

/**
 * Class PhotoGallery.
 *
 * Handles the recycler view for Photo Gallery.
 */
class PhotoGallery(
    private val invokingActivity: uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity,
    private val tripPlannerViewModel: TripPlannerViewModel)
    : GalleryPhotoAdapter.GalleryPhotoItemSelectedListener {

        private lateinit var galleryPhotoRecyclerView: RecyclerView
        private lateinit var galleryPhotoAdapter: GalleryPhotoAdapter

    init {
        setupGalleryPhotoRecyclerView()
    }

    /**
     * Sorts photos by the chosen sorting option.
     */
    fun changePhotos(photoSortingOption: PhotoSortingOption) {
        galleryPhotoAdapter.updateData(photoSortingOption)
    }

    /**
     * Reverses photo list if reverse button is clicked.
     */
    fun reversePhotoOrder(){
        galleryPhotoAdapter.reverseOrder()
    }

    /**
     * Trip RecyclerView setup. Observes Photo data in the database and
     * displays in the RecyclerView.
     */
    private fun setupGalleryPhotoRecyclerView(){
        val NUMBER_OF_COLUMNS = 3

        galleryPhotoRecyclerView = invokingActivity.findViewById(R.id.activity_photo_gallery_rv_photos)
        galleryPhotoAdapter = GalleryPhotoAdapter(this, tripPlannerViewModel)
        galleryPhotoRecyclerView.adapter = galleryPhotoAdapter
        galleryPhotoRecyclerView.layoutManager = GridLayoutManager(invokingActivity, NUMBER_OF_COLUMNS)

        tripPlannerViewModel.allPhotos.observe(invokingActivity){
            // If no photos exist, display a snackbar
            if(it.isEmpty()){
                invokingActivity.displaySnackbar(galleryPhotoRecyclerView, R.string.no_photos_snackbar)
            }
            it?.let {
                galleryPhotoAdapter.submitList(it)
            }
        }
    }

    /**
     * Handles behaviour, which happens when a Photo is clicked.
     * Launches the PhotoDetailsActivity with selected Photo's id in the intent.
     */
    override fun onGalleryPhotoItemSelected(photo: Photo, photoView: View) {
        val intent = Intent(invokingActivity, PhotoDetailsActivity::class.java)
        intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, photo.photoId)
        invokingActivity.startActivity(intent)
    }
}
