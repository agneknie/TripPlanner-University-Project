package uk.ac.shef.oak.com4510.data.access.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import uk.ac.shef.oak.com4510.data.access.daos.LocationDao
import uk.ac.shef.oak.com4510.data.access.entities.LocationEntity
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Trip
import java.time.LocalDateTime

class LocationRepository(private val locationDao: LocationDao) {

    // All locations
    val allLocations: LiveData<List<LocationEntity>> = locationDao.getAllLocations().asLiveData()

    // All locations with photos
    val photoLocations: LiveData<List<LocationEntity>> = locationDao.getLocationsContainingPhotos().asLiveData()

    // Last tracked/inserted in database location
    val lastLocationId: LiveData<Int> = locationDao.getLastLocationId().asLiveData()

    /**
     * Get all locations belonging to a trip.
     */
    fun getLocationsByTrip(trip: Trip) =
        locationDao.getLocationsByTrip(trip.tripId).asLiveData()

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
        dateTime = LocalDateTime.parse(dateTime),
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

/**
 * Maps List of Location to List of LocationEntity.
 */
fun List<Location>.asDatabaseEntities(): List<LocationEntity>{
    return map{
        it.asDatabaseEntity()
    }
}
//endregion