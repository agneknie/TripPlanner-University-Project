package com.example.tripplanner.models

import android.net.Uri

data class Photo (
    val photoId: Int = 0,
    val photoPath: Uri,
    val locationId: Int,
    var title: String? = null,
    var description: String? = null,
    var tagId: Int? = null,
    var thumbnailPath: Uri? = null){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (photoId != other.photoId) return false
        if (photoPath != other.photoPath) return false
        if (locationId != other.locationId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (tagId != other.tagId) return false
        if(thumbnailPath!= other.thumbnailPath) return false

        return true
    }
}