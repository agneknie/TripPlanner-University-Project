package com.example.tripplanner.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tripplanner.data.access.repositories.TripRepository

class TripCreationViewModel (
    private val tripRepository: TripRepository,
    private val applicationContext: Application) : AndroidViewModel(applicationContext){
        // TODO Finish this and other view models
}