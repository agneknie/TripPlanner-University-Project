package uk.ac.shef.oak.com4510.components

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.components.adapters.GalleryPhotoAdapter
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import uk.ac.shef.oak.com4510.views.PhotoDetailsActivity

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
     * Reverses the order of the photos in the gallery.
     */
    fun reversePhotos(){
        galleryPhotoAdapter.reverse()
    }

    private fun setupGalleryPhotoRecyclerView(){
        val NUMBER_OF_COLUMNS = 3

        galleryPhotoRecyclerView = invokingActivity.findViewById(R.id.activity_photo_gallery_rv_photos)
        galleryPhotoAdapter = GalleryPhotoAdapter(this)
        galleryPhotoRecyclerView.adapter = galleryPhotoAdapter
        galleryPhotoRecyclerView.layoutManager = GridLayoutManager(invokingActivity, NUMBER_OF_COLUMNS)

        tripPlannerViewModel.allPhotos.observe(invokingActivity){
            it?.let {
                galleryPhotoAdapter.submitList(it)
            }
        }
    }

    override fun onGalleryPhotoItemSelected(photo: Photo, photoView: View) {
        val intent = Intent(invokingActivity, PhotoDetailsActivity::class.java)
        intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, photo.photoId)
        invokingActivity.startActivity(intent)
    }
}
