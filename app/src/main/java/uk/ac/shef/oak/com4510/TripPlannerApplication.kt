package uk.ac.shef.oak.com4510

import android.app.Application
import uk.ac.shef.oak.com4510.data.access.database.AppDatabase
import uk.ac.shef.oak.com4510.data.access.repositories.LocationRepository
import uk.ac.shef.oak.com4510.data.access.repositories.PhotoRepository
import uk.ac.shef.oak.com4510.data.access.repositories.TagRepository
import uk.ac.shef.oak.com4510.data.access.repositories.TripRepository

/**
 * Class TripPlannerApplication.
 *
 * Initialises the application via initialising
 * the database and repositories of the application.
 */
class TripPlannerApplication: Application() {

    // Initialising database
    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    // Initialising Trip Repository
    val tripRepository: TripRepository by lazy {
        TripRepository(database.tripDao())
    }

    // Initialising Location Repository
    val locationRepository: LocationRepository by lazy{
        LocationRepository(database.locationDao())
    }

    // Initialising Photo Repository
    val photoRepository: PhotoRepository by lazy {
        PhotoRepository(database.photoDao())
    }

    // Initialising Tag Repository
    val tagRepository: TagRepository by lazy {
        TagRepository(database.tagDao())
    }
}