package uk.ac.shef.oak.com4510.data.access.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import uk.ac.shef.oak.com4510.data.access.daos.PhotoDao
import uk.ac.shef.oak.com4510.data.access.entities.PhotoEntity
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Photo

/**
 * Class PhotoRepository.
 *
 * Implements the repository for the Photo model, matching the queries in
 * the PhotoDao.
 */
class PhotoRepository(private val photoDao: PhotoDao) {

    // Observable for all photos
    val photos: LiveData<List<PhotoEntity>> = photoDao.getAllPhotos().asLiveData()

    /**
     * Get Photo by its photoId.
     */
    fun getPhoto(photoId: Int) = photoDao.getPhoto(photoId).asLiveData()

    /**
     * Get Photo by its Location.
     */
    fun getPhotoByLocation(location: Location) = photoDao.getPhotoByLocation(location.locationId).asLiveData()

    /**
     * Get List of Photos belonging to a Trip.
     */
    fun getPhotosByTripId(tripId: Int) = photoDao.getPhotosByTripId(tripId).asLiveData()

    /**
     * Get number of Photos associated with a Trip.
     */
    fun getPhotoCountByTrip(tripId: Int) = photoDao.getPhotoCountByTripId(tripId).asLiveData()

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
        tagId = tagId,
        thumbnailPath = Uri.parse(thumbnailPath))
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
//endregion