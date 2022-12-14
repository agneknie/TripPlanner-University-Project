package uk.ac.shef.oak.com4510.data.access.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import uk.ac.shef.oak.com4510.data.access.entities.PhotoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface PhotoDao.
 *
 * Provides Photo database query structure.
 */
@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo ORDER BY photo_id ASC")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photo WHERE photo_id = :photoId")
    fun getPhoto(photoId: Int): Flow<PhotoEntity>

    @Query("SELECT * FROM photo WHERE location_id = :locationId")
    fun getPhotoByLocation(locationId: Int): Flow<PhotoEntity>

    @Query("SELECT * FROM photo ORDER BY location_id ASC")
    fun getAllPhotosByLocation(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photo WHERE tag_id = :tagId")
    fun getPhotosByTag(tagId: Int): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photo ORDER BY tag_id ASC")
    fun getAllPhotosByTag(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photo WHERE location_id IN (SELECT location_id FROM location WHERE trip_id = :tripId)")
    fun getPhotosByTripId(tripId: Int): Flow<List<PhotoEntity>>

    @Query("SELECT COUNT(photo_id) FROM photo WHERE location_id IN (SELECT location_id FROM location WHERE trip_id = :tripId)")
    fun getPhotoCountByTripId(tripId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photoEntity: PhotoEntity)

    @Update
    suspend fun updatePhoto(photoEntity: PhotoEntity)
}