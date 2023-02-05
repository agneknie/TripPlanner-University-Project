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

/**
 * Class PhotoAdapter.
 *
 * Provides adapter(ListAdapter) functionality to the Photos RecyclerView in
 * TripOverviewActivity
 */
class TripPhotoAdapter (
    val tripPhotoItemClickListener: TripPhotoItemClickListener): ListAdapter<Photo, TripPhotoAdapter.TripPhotoViewHolder>(
    TripPhotoComparator())
{
    lateinit var context: Context

    /**
     * Click listener, which when implemented, allows access to the
     * clicked photo in the implementing activity.
     */
    interface TripPhotoItemClickListener{
        /**
         * Gets information about a Photo, which was clicked in the Trip RecyclerView.
         * Opens PhotoDetailsActivity with selected Photo's information.
         */
        fun onTripPhotoItemClick(photo: Photo, photoView: View)
    }

    /**
     * Provides a reference to the type of views that photos are using.
     */
    inner class TripPhotoViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val photoView: ImageView = view.findViewById(R.id.trip_photo_view_iv_photo)

        /**
         * Initialises photoView and configures its onClickListener.
         */
        fun bind(photo: Photo){
            photoView.setImageURI(photo.thumbnailPath)
            photoView.setOnClickListener{
                // Notifies that tag was selected
                tripPhotoItemClickListener.onTripPhotoItemClick(photo, photoView)
            }
        }
    }

    /**
     * Comparator for Photos, required by ListAdapter.
     */
    class TripPhotoComparator: DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
            return oldPhoto.photoId === newPhoto.photoId
        }

        override fun areContentsTheSame(oldPhoto: Photo, newPhoto: Photo): Boolean {
            return oldPhoto == newPhoto
        }

    }

    /**
     * Creates the photoView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripPhotoViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.trip_photo_view,
            parent,
            false
        )
        return TripPhotoViewHolder(view)
    }

    /**
     * Replaces contents of the photoView.
     */
    override fun onBindViewHolder(holder: TripPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}