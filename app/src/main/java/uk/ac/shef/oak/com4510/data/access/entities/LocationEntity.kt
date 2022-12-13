package uk.ac.shef.oak.com4510.data.access.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Class LocationEntity.
 *
 * Defines Location model as a database entity.
 */
@Entity(tableName = "location", indices = [Index(value = ["location_id"])])
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "location_id") val locationId: Int = 0,
    @ColumnInfo(name = "x_coordinate") val xCoordinate: Double,
    @ColumnInfo(name = "y_coordinate") val yCoordinate: Double,
    @ColumnInfo(name = "temperature") val temperature: Int,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "date_time") val dateTime: String,
    @ColumnInfo(name = "trip_id") val tripId: Int)