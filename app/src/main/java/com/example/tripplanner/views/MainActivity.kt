package com.example.tripplanner.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity

class MainActivity : TripPlannerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}