package uk.ac.shef.oak.com4510.data.access.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import uk.ac.shef.oak.com4510.data.access.entities.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo ORDER BY photo_id ASC")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photo WHERE photo_id = :photoId")
    fun getPhoto(photoId: Int): Flow<PhotoEntity>

    @Query("SELECT * FROM photo WHERE location_id = :locationId")
    fun getPhotoByLocation(locationId: Int): Flow<PhotoEntity>

    @Query("SELECT * FROM photo WHERE tag_id = :tagId")
    fun getPhotosByTag(tagId: Int): Flow<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photoEntity: PhotoEntity)

    @Update
    suspend fun updatePhoto(photoEntity: PhotoEntity)
}