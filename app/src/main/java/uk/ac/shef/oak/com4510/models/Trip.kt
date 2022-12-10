package uk.ac.shef.oak.com4510.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Class Trip.
 *
 * Defines the Trip model.
 */
data class Trip(
    val tripId: Int = 0,
    val startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime? = null,
    var title: String,
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

    /**
     * Gets trip length as a string in the format of:
     * 0h 0mins
     */
    fun getLengthAsString(): String{
        val lengthMinutes = getLengthInMinutes()
        val hours = lengthMinutes/60
        val minutes = lengthMinutes%60

        val hourLabel = "h"
        val minuteLabel = "min"

        return "${hours}${hourLabel} ${minutes}${minuteLabel}"
    }

    /**
     * Returns start time as a string, formatted to yyyy-MM-dd HH:mm:ss.
     */
    fun getStartTimeAsString(): String{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return startDateTime.format(formatter)
    }

    /**
     * Returns end time as a string, formatted to yyyy-MM-dd HH:mm:ss.
     */
    fun getEndTimeAsString(): String{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return endDateTime?.format(formatter) ?: "N/A"
    }

    /**
     * Calculates Trip length in minutes.
     */
    private fun getLengthInMinutes(): Long{
        if(endDateTime == null) return 0

        return ChronoUnit.MINUTES.between(startDateTime, endDateTime)
    }
}