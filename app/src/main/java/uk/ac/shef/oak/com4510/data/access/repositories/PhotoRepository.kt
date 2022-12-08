package uk.ac.shef.oak.com4510.data.access.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import uk.ac.shef.oak.com4510.data.access.daos.PhotoDao
import uk.ac.shef.oak.com4510.data.access.entities.PhotoEntity
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Photo
import uk.ac.shef.oak.com4510.models.Tag

class PhotoRepository(private val photoDao: PhotoDao) {

    // Observable for all photos
    val allPhotos: LiveData<List<PhotoEntity>> = photoDao.getAllPhotos().asLiveData()

    /**
     * Get Photo by its photoId.
     */
    fun getPhoto(photoId: Int) = photoDao.getPhoto(photoId).asLiveData()

    /**
     * Get Photo by its Location.
     */
    fun getPhotoByLocation(location: Location) = photoDao.getPhotoByLocation(location.locationId).asLiveData()

    /**
     * Get List of Photos by Tag.
     */
    fun getPhotosByTag(tag: Tag) = photoDao.getPhotosByTag(tag.tagId).asLiveData()

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
}

//region Object Mapping
/**
 * Maps PhotoEntity to Photo.
 */
fun PhotoEntity.asDomainModel(): Photo {
    return Photo(
        photoId = photoId,
        photoPath = Uri.parse(photoPath),
        locationId = locationId,
        title = title,
        description = description,
        thumbnailPath = Uri.parse(thumbnailPath))
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
fun Photo.asDatabaseEntity(): PhotoEntity {
    return PhotoEntity(
        photoId = photoId,
        photoPath = photoPath.toString(),
        locationId = locationId,
        title = title,
        description = description,
        tagId = tagId,
        thumbnailPath = thumbnailPath.toString()
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