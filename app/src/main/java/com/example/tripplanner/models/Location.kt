package com.example.tripplanner.models

import java.time.LocalDateTime

data class Location(
    val locationId: Int = 0,
    val xCoordinate: Double,
    val yCoordinate: Double,
    val temperature: Int,
    val pressure: Int,
    val dateTime: LocalDateTime,
    val tripId: Int){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (locationId != other.locationId) return false
        if (xCoordinate != other.xCoordinate) return false
        if (yCoordinate != other.yCoordinate) return false
        if (temperature != other.temperature) return false
        if (pressure != other.pressure) return false
        if (dateTime != other.dateTime) return false
        if (tripId != other.tripId) return false

        return true
    }
}