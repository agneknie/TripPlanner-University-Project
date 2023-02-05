package uk.ac.shef.oak.com4510.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.shef.oak.com4510.data.access.repositories.LocationRepository
import uk.ac.shef.oak.com4510.data.access.repositories.PhotoRepository
import uk.ac.shef.oak.com4510.data.access.repositories.TagRepository
import uk.ac.shef.oak.com4510.data.access.repositories.TripRepository
import java.lang.IllegalArgumentException

/**
 * Class TripPlannerViewModelFactory.
 *
 * A factory for creating TripPlannerViewModel.
 */
class TripPlannerViewModelFactory(
    private val tripRepository: TripRepository,
    private val locationRepository: LocationRepository,
    private val photoRepository: PhotoRepository,
    private val tagRepository: TagRepository,
    private val applicationContext: Application
): ViewModelProvider.Factory{

    /**
     * Creates the TripPlannerViewModel class.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TripPlannerViewModel::class.java)){
            return TripPlannerViewModel(
                tripRepository,
                locationRepository,
                photoRepository,
                tagRepository,
                applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
    }
}