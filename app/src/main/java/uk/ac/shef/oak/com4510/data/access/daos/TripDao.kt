package uk.ac.shef.oak.com4510.data.access.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import uk.ac.shef.oak.com4510.data.access.entities.TripEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface TripDao.
 *
 * Provides Trip database query structure.
 */
@Dao
interface TripDao {
    @Query("SELECT * FROM trip ORDER BY trip_id ASC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trip WHERE trip_id = :tripId")
    fun getTrip(tripId: Int): Flow<TripEntity>

    @Query("SELECT * FROM trip WHERE tag_id = :tagId")
    fun getTripsByTag(tagId: Int): Flow<TripEntity>

    @Query("SELECT trip_id FROM trip ORDER BY trip_id DESC LIMIT 1")
    fun getCurrentTripId(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(tripEntity: TripEntity)

    @Update
    suspend fun updateTrip(tripEntity: TripEntity)
}