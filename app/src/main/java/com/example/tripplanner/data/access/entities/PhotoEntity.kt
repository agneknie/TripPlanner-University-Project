package com.example.tripplanner.data.access.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "photo", indices = [Index(value = ["photo_id"])])
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "photo_id") val photoId: Int = 0,
    @ColumnInfo(name = "photo_path") val photoPath: String,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "tag_id") var tagId: Int
)