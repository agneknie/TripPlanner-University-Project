package com.example.tripplanner.data.access.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tripplanner.data.access.daos.LocationDao
import com.example.tripplanner.data.access.entities.LocationEntity
import com.example.tripplanner.models.Location
import java.time.LocalDateTime

class LocationRepository(private val locationDao: LocationDao) {

    // Observable for all locations
    val locations: LiveData<List<LocationEntity>> = locationDao.getAllLocations().asLiveData();

    /**
     * Insert Location in the database.
     */
    suspend fun insertLocation(location: Location){
        locationDao.insertLocation(location.asDatabaseEntity())
    }

    // TODO Add other necessary functions based on Dao

    //region Object Mapping
    /**
     * Maps LocationEntity to Location.
     */
    private fun LocationEntity.asDomainModel(): Location{
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
    private fun List<LocationEntity>.asDomainModels(): List<Location>{
        return map{
            it.asDomainModel()
        }
    }

    /**
     * Maps Location to LocationEntity.
     */
    private fun Location.asDatabaseEntity(): LocationEntity{
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
    private fun List<Location>.asDatabaseEntities(): List<LocationEntity>{
        return map{
            it.asDatabaseEntity()
        }
    }
    //endregion
}