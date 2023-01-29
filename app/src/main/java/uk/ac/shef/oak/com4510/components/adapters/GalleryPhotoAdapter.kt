package uk.ac.shef.oak.com4510.components.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.helpers.PhotoSortingOption
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel

/**
 * Class GalleryPhotoAdapter.
 *
 * Provides adapter(ListAdapter) functionality to the Photo RecyclerView.
 */
class GalleryPhotoAdapter (
    val galleryPhotoItemSelectedListener: GalleryPhotoItemSelectedListener,
    val tripPlannerViewModel: TripPlannerViewModel): ListAdapter<Photo, GalleryPhotoAdapter.GalleryPhotoViewHolder>(
    GalleryPhotoComparator())
{
    lateinit var context: Context

    //region Photo List Sorting
    /**
     * Updates the photo list in the adapter, sorting it with the
     * provided sorting option.
     */
    fun updateData(photoSortingOption: PhotoSortingOption){
        val currentListThis = currentList
        val list: List<Photo>

        when (photoSortingOption) {
            PhotoSortingOption.DEFAULT -> {
                list = currentListThis.sortedBy { it.photoId }
            }
            PhotoSortingOption.LOCATION -> {
                list = currentListThis.sortedBy { it.locationId }
            }
            PhotoSortingOption.TAG -> {
                list = currentListThis.sortedBy { it.tagId }
            }
            PhotoSortingOption.TITLE -> {
                list = currentListThis.sortedBy { it.title }
            }
            PhotoSortingOption.DESCRIPTION -> {
                list = currentListThis.sortedBy { it.description }
            }
        }
        submitList(list)
    }

    /**
     * Reverses order of the current photo list.
     */
    fun reverseOrder(){
        val currentListThis = currentList
        val list = currentListThis.reversed()
        submitList(list)
    }
    //endregion

    //region Photo Clicking Implementation
    interface GalleryPhotoItemSelectedListener{
        fun onGalleryPhotoItemSelected(photo: Photo, photoView: View)
    }

    inner class GalleryPhotoViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val photoView: ImageView = view.findViewById(R.id.gallery_photo_view_iv_photo)

        /**
         * Initialises photoView and configures its onClickListener.
         */
        fun bind(photo: Photo){
            photoView.setImageURI(photo.thumbnailPath)
            photoView.setOnClickListener{
                // Notifies that tag was selected
                galleryPhotoItemSelectedListener.onGalleryPhotoItemSelected(photo, photoView)
            }
        }
    }
    //endregion

    //region Comparator Class
    class GalleryPhotoComparator: DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
            return oldPhoto.photoId === newPhoto.photoId
        }

        override fun areContentsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
            return oldPhoto == newPhoto
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPhotoViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.gallery_photo_view,
            parent,
            false
        )
        return GalleryPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    //endregion
}