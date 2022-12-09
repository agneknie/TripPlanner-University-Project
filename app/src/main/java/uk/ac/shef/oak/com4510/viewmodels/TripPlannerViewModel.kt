package uk.ac.shef.oak.com4510.viewmodels

import android.app.Application
import androidx.lifecycle.*
import uk.ac.shef.oak.com4510.data.access.repositories.*
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Tag
import uk.ac.shef.oak.com4510.models.Trip
import kotlinx.coroutines.launch
import uk.ac.shef.oak.com4510.models.Photo
import java.lang.IllegalArgumentException

/**
 * Class TripPlannerViewModel.
 *
 * ViewModel of the application. Handles database querying through room.
 * Accesses TripRepository, LocationRepository, PhotoRepository & TagRepository.
 */
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

    // Get current Location id from the database.
    val lastLocationId: LiveData<Int> = locationRepository.lastLocationId

    /**
     * Get Trip from the database.
     */
    fun getTrip(tripId: Int): LiveData<Trip> = Transformations.map(tripRepository.getTrip(tripId)){
        it.asDomainModel()
    }

    /**
     * Get Photo from the database.
     */
    fun getPhoto(photoId: Int): LiveData<Photo> = Transformations.map(photoRepository.getPhoto(photoId)){
        it.asDomainModel()
    }

    /**
     * Get Location from the database.
     */
    fun getLocation(locationId: Int): LiveData<Location> = Transformations.map(locationRepository.getLocation(locationId)){
        it.asDomainModel()
    }

    /**
     * Get Tag from the database.
     */
    fun getTag(tagId: Int): LiveData<Tag> = Transformations.map(tagRepository.getTag(tagId)){
        it.asDomainModel()
    }

    /**
     * Gets all Locations belonging to a trip in a sorted list, where
     * the first location is the start location and the last location is the
     * end location of a Trip.
     */
    fun getLocationsByTrip(tripId: Int): LiveData<List<Location>> = Transformations.map(locationRepository.getLocationsByTrip(tripId)){
        it.asDomainModels()
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
     * Insert given Photo in the database.
     */
    fun insertPhoto(photo: Photo) = viewModelScope.launch {
        photoRepository.insertPhoto(photo)
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

    /**
     * Update given Photo in the database.
     */
    fun updatePhoto(photo: Photo) = viewModelScope.launch {
        photoRepository.updatePhoto(photo)
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