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
import uk.ac.shef.oak.com4510.models.Photo

class GalleryPhotoAdapter (
    val galleryPhotoItemSelectedListener: GalleryPhotoItemSelectedListener): ListAdapter<Photo, GalleryPhotoAdapter.GalleryPhotoViewHolder>(
    GalleryPhotoComparator())
{
    private val photos = mutableListOf<Photo>()
    fun updateData(newPhoto: List<Photo>) {
        photos.clear()
        photos.addAll(newPhoto)
        notifyDataSetChanged()
    }
    lateinit var context: Context

    interface GalleryPhotoItemSelectedListener{
        fun onGalleryPhotoItemSelected(photo: Photo, photoView: View)
    }

    inner class GalleryPhotoViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val photoView: ImageView = view.findViewById(R.id.activity_photo_gallery_rv_photos)

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

    fun clear(){
        photos.clear()
        notifyDataSetChanged()
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
            R.layout.trip_photo_view,
            parent,
            false
        )
        return GalleryPhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}