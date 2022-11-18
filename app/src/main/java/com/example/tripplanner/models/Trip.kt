package com.example.tripplanner.models

data class Trip(
    val tripId: Int = 0,
    val startDateTime: String,
    var endDateTime: String? = null,
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