package uk.ac.shef.oak.com4510.data.access.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import uk.ac.shef.oak.com4510.data.access.daos.TripDao
import uk.ac.shef.oak.com4510.data.access.entities.TripEntity
import uk.ac.shef.oak.com4510.models.Trip
import java.time.LocalDateTime

/**
 * Class TripRepository.
 *
 * Implements the repository for the Trip model, matching the queries in
 * the TripDao.
 */
class TripRepository(private val tripDao: TripDao) {

    // Observable for all trips
    val trips: LiveData<List<TripEntity>> = tripDao.getAllTrips().asLiveData()

    /**
     * Get current tripId from the database.
     */
    fun getCurrentTripId() = tripDao.getCurrentTripId().asLiveData()

    /**
     * Get Trip by its tripId.
     */
    fun getTrip(tripId: Int) = tripDao.getTrip(tripId).asLiveData()

    /**
     * Insert a Trip in the database.
     */
    suspend fun insertTrip(trip: Trip){
        tripDao.insertTrip(trip.asDatabaseEntity())
    }

    /**
     * Update a Trip in the database.
     */
    suspend fun updateTrip(trip: Trip){
        tripDao.updateTrip(trip.asDatabaseEntity())
    }
}
//region Object Mapping
/**
 * Maps TripEntity to Trip.
 */
fun TripEntity.asDomainModel(): Trip {
    return Trip(
        tripId = tripId,
        startDateTime = LocalDateTime.parse(startDateTime),
        endDateTime = if(endDateTime == "null") null else LocalDateTime.parse(endDateTime),
        title = title,
        tagId = tagId
    )
}

/**
 * Maps List of TripEntity to List of Trip.
 */
fun List<TripEntity>.asDomainModels(): List<Trip>{
    return map{
        it.asDomainModel()
    }
}

/**
 * Maps Trip to TripEntity.
 */
fun Trip.asDatabaseEntity(): TripEntity {
    return TripEntity(
        tripId = tripId,
        startDateTime = startDateTime.toString(),
        endDateTime = endDateTime.toString(),
        title = title,
        tagId = tagId
    )
}
//endregion