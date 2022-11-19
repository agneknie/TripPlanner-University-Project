package com.example.tripplanner.data.access.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tripplanner.data.access.daos.PhotoDao
import com.example.tripplanner.data.access.entities.PhotoEntity
import com.example.tripplanner.models.Photo

class PhotoRepository(private val photoDao: PhotoDao) {

    // Observable for all photos
    val photos: LiveData<List<PhotoEntity>> = photoDao.getAllPhotos().asLiveData()

    /**
     * Insert a Photo in the database.
     */
    suspend fun insertPhoto(photo: Photo){
        photoDao.insertPhoto(photo.asDatabaseEntity())
    }

    /**
     * Update a Photo in the database.
     */
    suspend fun updatePhoto(photo: Photo){
        photoDao.updatePhoto(photo.asDatabaseEntity())
    }

    // TODO Add other necessary functions based on Dao
}

//region Object Mapping
/**
 * Maps PhotoEntity to Photo.
 */
fun PhotoEntity.asDomainModel(): Photo{
    return Photo(
        photoId = photoId,
        photoPath = Uri.parse(photoPath),
        locationId = locationId,
        title = title,
        description = description)
    // TODO photo.getOrMakeThumbnail(context) and then add context: Context
}

/**
 * Maps List of PhotoEntity to List of Photo.
 */
fun List<PhotoEntity>.asDomainModels(): List<Photo>{
    return map{
        it.asDomainModel()
    }
}

/**
 * Maps Photo to PhotoEntity.
 */
fun Photo.asDatabaseEntity(): PhotoEntity{
    return PhotoEntity(
        photoId = photoId,
        photoPath = photoPath.toString(),
        locationId = locationId,
        title = title,
        description = description,
        tagId = tagId
    )
}

/**
 * Maps List of Photo to List of PhotoEntity.
 */
fun List<Photo>.asDatabaseEntities(): List<PhotoEntity>{
    return map{
        it.asDatabaseEntity()
    }
}

//endregion