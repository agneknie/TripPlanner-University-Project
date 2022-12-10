package uk.ac.shef.oak.com4510.components.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.models.Trip

/**
 * Class TripAdapter.
 *
 * Provides adapter(ListAdapter) functionality to the Trips RecyclerView
 */
class TripAdapter (
    val tripItemClickListener: TripItemClickListener): ListAdapter<Trip, TripAdapter.TripViewHolder>(
    TripComparator())
{
    lateinit var context: Context

    /**
     * Click listener, which when implemented, allows access to the
     * clicked trip in the implementing activity.
     */
    interface TripItemClickListener{
        /**
         * Gets information about a Trip, which was clicked in the Trip RecyclerView.
         * Opens TripOverviewActivity with selected Trip's information.
         */
        fun onTripItemClick(trip: Trip, tripView: View)
    }

    /**
     * Provides a reference to the type of views that trips are using.
     */
    inner class TripViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tripView: TextView = view.findViewById(R.id.trip_view_tv_trip_name)

        /**
         * Initialises tripView and configures its onClickListener.
         */
        fun bind(trip: Trip){
            tripView.text = trip.title
            tripView.setOnClickListener{
                // Notifies that tag was selected
                tripItemClickListener.onTripItemClick(trip, tripView)
            }
        }
    }

    /**
     * Comparator for Trips, required by ListAdapter.
     */
    class TripComparator: DiffUtil.ItemCallback<Trip>(){
        override fun areItemsTheSame(oldTrip: Trip, newTrip: Trip): Boolean {
            return oldTrip.tripId === newTrip.tripId
        }

        override fun areContentsTheSame(oldTrip: Trip, newTrip: Trip): Boolean {
            return oldTrip.equals(newTrip)
        }

    }

    /**
     * Creates the tripView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.trip_view,
            parent,
            false
        )
        return TripViewHolder(view)
    }

    /**
     * Replaces contents of the tripView.
     */
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}