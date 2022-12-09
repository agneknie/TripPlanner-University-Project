package uk.ac.shef.oak.com4510.views

import android.content.Intent
import android.os.Bundle
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoMapBinding
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import java.time.LocalDateTime

class PhotoMapActivity: TripPlannerAppCompatActivity()  {
    private lateinit var binding: ActivityPhotoMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Insert map into activity_trip_trip_ll_map_holder. Set inserted map's height & width accordingly if necessary.
        //  Currently width is match_parent & height is 200dp (testing purposes)

        // Gets all locations
        tripPlannerViewModel.photoLocations.observe(this){
            for (photoLocation in it){
                // Puts a location marker on the map
                putLocationMarkerOnMap(photoLocation)
            }
        }

        // TODO Remove. Placeholder so I can test the PhotoDisplay & PhotoDetails activities behaviour
        binding.activityPhotoMapLlMapHolder.setOnClickListener {
            locationClicked(Location(1, 0.0, 0.0, 0, 0, LocalDateTime.now(), 0))
        }
    }

    /**
     * Implements location click functionality. When a location marker is clicked,
     * retrieves associated photo from the database and starts activity, which
     * displays its details.
     *
     * location: Location object, associated with the clicked location on the map.
     *
     * // TODO Use this when a location marker is clicked
     */
    private fun locationClicked(location: Location){
        tripPlannerViewModel.getPhotoByLocation(location).observe(this){
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(IntentKeys.SELECTED_PHOTO_ID, it.photoId)
            startActivity(intent)
        }
    }

    /**
     * Given a Location object, puts a marker with that location on the map.
     */
    private fun putLocationMarkerOnMap(location: Location){
        // TODO Puts location marker on the map

        // TODO Calls locationClicked when location marker is clicked, passing the location
    }
}