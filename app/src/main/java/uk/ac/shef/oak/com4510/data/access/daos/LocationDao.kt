package uk.ac.shef.oak.com4510.data.access.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.shef.oak.com4510.data.access.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location ORDER BY location_id ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE trip_id = :tripId ORDER BY location_id ASC")
    fun getLocationsByTrip(tripId: Int): Flow<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE location_id IN (SELECT location_id FROM photo)")
    fun getLocationsContainingPhotos(): Flow<List<LocationEntity>>

    @Query("SELECT location_id FROM location ORDER BY location_id DESC LIMIT 1")
    fun getLastLocationId(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationEntity: LocationEntity)
}