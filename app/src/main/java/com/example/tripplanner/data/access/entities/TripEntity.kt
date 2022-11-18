package com.example.tripplanner.data.access.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "trip", indices = [Index(value = ["trip_id"])])
data class TripEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "trip_id") val tripId: Int = 0,
    @ColumnInfo(name = "start_date_time") val startDateTime: String,
    @ColumnInfo(name = "end_date_time") var endDateTime: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "tag_id") var tagId: Int)