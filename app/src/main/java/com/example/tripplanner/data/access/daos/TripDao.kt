package com.example.tripplanner.data.access.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tripplanner.data.access.entities.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trip ORDER BY trip_id ASC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trip WHERE trip_id = :tripId")
    fun getTrip(tripId: Int): Flow<TripEntity>

    @Query("SELECT * FROM trip WHERE tag_id = :tagId")
    fun getTripsByTag(tagId: Int): Flow<TripEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(tripEntity: TripEntity)

    @Update
    suspend fun updateTrip(tripEntity: TripEntity)
}