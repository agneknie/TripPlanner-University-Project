package uk.ac.shef.oak.com4510.viewmodels

import android.app.Application
import androidx.lifecycle.*
import uk.ac.shef.oak.com4510.data.access.repositories.*
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Tag
import uk.ac.shef.oak.com4510.models.Trip
import kotlinx.coroutines.launch
import uk.ac.shef.oak.com4510.models.Photo

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
    applicationContext: Application): AndroidViewModel(applicationContext){

    //region Trip Related
    // All trips in the repository
    val allTrips: LiveData<List<Trip>> = Transformations.map(tripRepository.trips){
        it.asDomainModels()
    } as MutableLiveData<List<Trip>>

    // Get current Trip id from the database.
    val currentTripId: LiveData<Int> = tripRepository.getCurrentTripId()

    /**
     * Get Trip from the database.
     */
    fun getTrip(tripId: Int): LiveData<Trip> = Transformations.map(tripRepository.getTrip(tripId)){
        it.asDomainModel()
    }

    /**
     * Insert given Trip in the database.
     */
    fun insertTrip(trip: Trip) = viewModelScope.launch {
        tripRepository.insertTrip(trip)
    }

    /**
     * Update given Trip in the database.
     */
    fun updateTrip(trip: Trip) = viewModelScope.launch {
        tripRepository.updateTrip(trip)
    }
    //endregion

    //region Photo Related
    val allPhotos: LiveData<List<Photo>> = Transformations.map(photoRepository.photos){
        it.asDomainModels()
    } as MutableLiveData<List<Photo>>

    /**
     * Get Photo from the database.
     */
    fun getPhoto(photoId: Int): LiveData<Photo> = Transformations.map(photoRepository.getPhoto(photoId)){
        it.asDomainModel()
    }

    /**
     * Get Photo by its Location from the database.
     */
    fun getPhotoByLocation(location: Location): LiveData<Photo> = Transformations.map(photoRepository.getPhotoByLocation(location)){
        it.asDomainModel()
    }

    /**
     * Get all Photos associated to a Trip.
     */
    fun getPhotosByTripId(tripId: Int): LiveData<List<Photo>> = Transformations.map(photoRepository.getPhotosByTripId(tripId)){
        it.asDomainModels()
    }

    /**
     * Get count of photos in a Trip.
     */
    fun getPhotoCountByTrip(tripId: Int): LiveData<Int> = photoRepository.getPhotoCountByTrip(tripId)

    /**
     * Insert given Photo in the database.
     */
    fun insertPhoto(photo: Photo) = viewModelScope.launch {
        photoRepository.insertPhoto(photo)
    }

    /**
     * Update given Photo in the database.
     */
    fun updatePhoto(photo: Photo) = viewModelScope.launch {
        photoRepository.updatePhoto(photo)
    }
    //endregion

    //region Location Related
    // Get current Location id from the database.
    val lastLocationId: LiveData<Int> = locationRepository.lastLocationId

    // Get all locations, which have photos associated with them
    val photoLocations: LiveData<List<Location>> = Transformations.map(locationRepository.photoLocations){
        it.asDomainModels()
    } as MutableLiveData<List<Location>>

    /**
     * Get Location from the database.
     */
    fun getLocation(locationId: Int): LiveData<Location> = Transformations.map(locationRepository.getLocation(locationId)){
        it.asDomainModel()
    }

    /**
     * Get count of Locations in a Trip.
     */
    fun getLocationCountByTrip(tripId: Int): LiveData<Int> = locationRepository.getLocationCountByTrip(tripId)

    /**
     * Gets all Locations belonging to a trip in a sorted list, where
     * the first location is the start location and the last location is the
     * end location of a Trip.
     */
    fun getLocationsByTrip(tripId: Int): LiveData<List<Location>> = Transformations.map(locationRepository.getLocationsByTrip(tripId)){
        it.asDomainModels()
    }

    /**
     * Insert given Location in the database.
     */
    fun insertLocation(location: Location) = viewModelScope.launch {
        locationRepository.insertLocation(location)
    }
    //endregion

    //region Tag Related
    // All tags in the repository
    val allTags: LiveData<List<Tag>> = Transformations.map(tagRepository.tags){
        it.asDomainModel()
    } as MutableLiveData<List<Tag>>

    /**
     * Get Tag from the database.
     */
    fun getTag(tagId: Int): LiveData<Tag> = Transformations.map(tagRepository.getTag(tagId)){
        it.asDomainModel()
    }

    /**
     * Insert given Tag into the database.
     */
    fun insertTag(tag: Tag) = viewModelScope.launch {
        tagRepository.insertTag(tag)
    }
    //endregion
}