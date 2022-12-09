package uk.ac.shef.oak.com4510.data.access.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.shef.oak.com4510.data.access.entities.TagEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface TagDao.
 *
 * Provides Tag database query structure.
 */
@Dao
interface TagDao {
    @Query("SELECT * FROM tag ORDER BY tag_id ASC")
    fun getAllTags(): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tagEntity: TagEntity)
}