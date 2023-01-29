package uk.ac.shef.oak.com4510.data.access.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import uk.ac.shef.oak.com4510.data.access.daos.LocationDao
import uk.ac.shef.oak.com4510.data.access.entities.LocationEntity
import uk.ac.shef.oak.com4510.models.Location
import java.time.LocalDateTime

/**
 * Class LocationRepository.
 *
 * Implements the repository for the Location model, matching the queries in
 * the LocationDao.
 */
class LocationRepository(private val locationDao: LocationDao) {

    // All locations with photos
    val photoLocations: LiveData<List<LocationEntity>> = locationDao.getLocationsContainingPhotos().asLiveData()

    // Last tracked/inserted in database location
    val lastLocationId: LiveData<Int> = locationDao.getLastLocationId().asLiveData()

    /**
     * Get all locations belonging to a trip.
     */
    fun getLocationsByTrip(tripId: Int) =
        locationDao.getLocationsByTrip(tripId).asLiveData()

    /**
     * Get number of locations recorded in a trip.
     */
    fun getLocationCountByTrip(tripId: Int) =
        locationDao.getLocationCountByTrip(tripId).asLiveData()

    /**
     * Get location by its location id.
     */
    fun getLocation(locationId: Int) =
        locationDao.getLocation(locationId).asLiveData()

    /**
     * Insert Location in the database.
     */
    suspend fun insertLocation(location: Location){
        locationDao.insertLocation(location.asDatabaseEntity())
    }

}
//region Object Mapping
/**
 * Maps LocationEntity to Location.
 */
fun LocationEntity.asDomainModel(): Location {
    return Location(
        locationId = locationId,
        xCoordinate = xCoordinate,
        yCoordinate = yCoordinate,
        temperature = temperature,
        pressure = pressure,
        dateTime = LocalDateTime.parse(LocalDateTime.now().toString()),
        tripId = tripId
    )
}

/**
 * Maps List of LocationEntity to List of Location
 */
fun List<LocationEntity>.asDomainModels(): List<Location>{
    return map{
        it.asDomainModel()
    }
}

/**
 * Maps Location to LocationEntity.
 */
fun Location.asDatabaseEntity(): LocationEntity {
    return LocationEntity(
        locationId = locationId,
        xCoordinate = xCoordinate,
        yCoordinate = yCoordinate,
        temperature = temperature,
        pressure = pressure,
        dateTime = dateTime.toString(),
        tripId = tripId
    )
}
//endregion