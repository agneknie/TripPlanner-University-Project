package com.example.tripplanner.models

import java.time.LocalDateTime

data class Trip(
    val tripId: Int = 0,
    val startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime? = null,
    val title: String,
    var tagId: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Trip

        if (tripId != other.tripId) return false
        if (startDateTime != other.startDateTime) return false
        if (endDateTime != other.endDateTime) return false
        if (title != other.title) return false
        if (tagId != other.tagId) return false

        return true
    }
}