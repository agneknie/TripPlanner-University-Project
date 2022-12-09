package uk.ac.shef.oak.com4510.data.access.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Class TagEntity.
 *
 * Defines Tag model as a database entity.
 */
@Entity(tableName = "tag", indices = [Index(value = ["tag_id"])])
data class TagEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "tag_id") val tagId: Int = 0,
    @ColumnInfo(name = "tag_name") val tagName: String)