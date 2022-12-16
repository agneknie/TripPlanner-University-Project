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

class GalleryPhotoAdapter (
    val galleryPhotoItemSelectedListener: GalleryPhotoItemSelectedListener,
    val tripPlannerViewModel: TripPlannerViewModel,
    val invokingActivity: uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity): ListAdapter<Photo, GalleryPhotoAdapter.GalleryPhotoViewHolder>(
    GalleryPhotoComparator())
{
    fun updateData(photoSortingOption: PhotoSortingOption){
        when (photoSortingOption) {
            PhotoSortingOption.DEFAULT -> {
                val currentListThis = currentList
                val list = currentListThis.sortedBy { it.photoId }
                submitList(list)
            }
            PhotoSortingOption.LOCATION -> {
                val currentListThis = currentList
                val list = currentListThis.sortedBy { it.locationId }
                submitList(list)
            }
            PhotoSortingOption.TAG -> {
                val currentListThis = currentList
                val list = currentListThis.sortedBy { it.tagId }
                submitList(list)
            }
            PhotoSortingOption.TITLE -> {
                val currentListThis = currentList
                val list = currentListThis.sortedBy { it.title }
                submitList(list)
            }
            PhotoSortingOption.DESCRIPTION -> {
                val currentListThis = currentList
                val list = currentListThis.sortedBy { it.description }
                submitList(list)
            }
        }
    }

    fun reverseOrder(){
        val currentListThis = currentList
        val list = currentListThis.reversed()
        submitList(list)
    }

    lateinit var context: Context

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

    class GalleryPhotoComparator: DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
            return oldPhoto.photoId === newPhoto.photoId
        }

        override fun areContentsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
            return oldPhoto.equals(newPhoto)
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
}