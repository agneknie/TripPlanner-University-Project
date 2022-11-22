package com.example.tripplanner.data.access.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tripplanner.data.access.daos.TripDao
import com.example.tripplanner.data.access.entities.TripEntity
import com.example.tripplanner.models.Tag
import com.example.tripplanner.models.Trip
import java.time.LocalDateTime

class TripRepository(private val tripDao: TripDao) {

    // Observable for all trips
    val trips: LiveData<List<TripEntity>> = tripDao.getAllTrips().asLiveData()

    /**
     * Get Trip by its tripId.
     */
    fun getTrip(tripId: Int) = tripDao.getTrip(tripId).asLiveData()

    /**
     * Get a List of Trips by Tag.
     */
    fun getTripsByTag(tag: Tag) = tripDao.getTripsByTag(tag.tagId).asLiveData()

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
fun TripEntity.asDomainModel(): Trip{
    return Trip(
        tripId = tripId,
        startDateTime = LocalDateTime.parse(startDateTime),
        endDateTime = LocalDateTime.parse(endDateTime),
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
fun Trip.asDatabaseEntity(): TripEntity{
    return TripEntity(
        tripId = tripId,
        startDateTime = startDateTime.toString(),
        endDateTime = endDateTime.toString(),
        title = title,
        tagId = tagId
    )
}

/**
 * Maps List of Trip to List of TripEntity.
 */
fun List<Trip>.asDatabaseEntitites(): List<TripEntity>{
    return map{
        it.asDatabaseEntity()
    }
}
//endregion