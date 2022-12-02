package uk.ac.shef.oak.com4510.viewmodels

import android.app.Application
import androidx.lifecycle.*
import uk.ac.shef.oak.com4510.data.access.repositories.*
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Tag
import uk.ac.shef.oak.com4510.models.Trip
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TripPlannerViewModel (
    private val tripRepository: TripRepository,
    private val locationRepository: LocationRepository,
    private val photoRepository: PhotoRepository,
    private val tagRepository: TagRepository,
    private val applicationContext: Application): AndroidViewModel(applicationContext){

    // All tags in the repository
    val allTags: LiveData<List<Tag>> = Transformations.map(tagRepository.tags){
        it.asDomainModel()
    } as MutableLiveData<List<Tag>>

    // Get current Trip id from the database.
    val currentTripId: LiveData<Int> = tripRepository.getCurrentTripId()

    /**
     * Get Trip from the database.
     */
    fun getTrip(tripId: Int): LiveData<Trip> = Transformations.map(tripRepository.getTrip(tripId)){
        it.asDomainModel()
    }

    /**
     * Insert given Tag into the database.
     */
    fun insertTag(tag: Tag) = viewModelScope.launch {
        tagRepository.insertTag(tag)
    }

    /**
     * Insert given Trip in the database.
     */
    fun insertTrip(trip: Trip) = viewModelScope.launch {
        tripRepository.insertTrip(trip)
    }

    /**
     * Insert given Location in the database.
     */
    fun insertLocation(location: Location) = viewModelScope.launch {
        locationRepository.insertLocation(location)
    }

    /**
     * Update given Trip in the database.
     */
    fun updateTrip(trip: Trip) = viewModelScope.launch {
        tripRepository.updateTrip(trip)
    }
}

/**
 * Factory for creating TripPlannerViewModel.
 */
class TripPlannerViewModelFactory(
    private val tripRepository: TripRepository,
    private val locationRepository: LocationRepository,
    private val photoRepository: PhotoRepository,
    private val tagRepository: TagRepository,
    private val applicationContext: Application): ViewModelProvider.Factory{

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