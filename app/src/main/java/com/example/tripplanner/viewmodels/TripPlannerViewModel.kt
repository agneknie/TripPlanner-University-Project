package com.example.tripplanner.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.tripplanner.data.access.repositories.*
import java.lang.IllegalArgumentException

class TripPlannerViewModel (
    private val tripRepository: TripRepository,
    private val locationRepository: LocationRepository,
    private val photoRepository: PhotoRepository,
    private val tagRepository: TagRepository,
    private val applicationContext: Application): AndroidViewModel(applicationContext){
}

/**
 * Factory for creating TripPlannerViewModel.
 */
class TripPlannerViewModelFactory(
    private val tripRepository: TripRepository,
    private val locationRepository: LocationRepository,
    private val photoRepository: PhotoRepository,
    private val tagRepository: TagRepository,
    private val applicationContext: Application): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TripPlannerViewModel::class.java)){
            return TripPlannerViewModel(
                tripRepository,
                locationRepository,
                photoRepository,
                tagRepository,
                applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
    }
}