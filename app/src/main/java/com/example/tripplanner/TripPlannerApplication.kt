package com.example.tripplanner

import android.app.Application
import com.example.tripplanner.data.access.database.AppDatabase
import com.example.tripplanner.data.access.repositories.LocationRepository
import com.example.tripplanner.data.access.repositories.PhotoRepository
import com.example.tripplanner.data.access.repositories.TagRepository
import com.example.tripplanner.data.access.repositories.TripRepository

/**
 * Initialises the database and repositories of the application.
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