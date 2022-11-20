package com.example.tripplanner.views

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityTripCreationBinding
import com.example.tripplanner.models.Tag

class TripCreationActivity: TripPlannerAppCompatActivity()  {
    private lateinit var binding: ActivityTripCreationBinding
    private lateinit var tagRecyclerView: RecyclerView
    // TODO Add adapter view private lateinit var adapter: TagViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityTripCreationBtnAddTag.setOnClickListener{
            val tagNameString = binding.activityTripCreationEtTags.text.toString()
            if(tagNameString.isNotEmpty()){
                val newTag: Tag = Tag(0,tagNameString)
                tripPlannerViewModel.insertTag(newTag)
                binding.activityTripCreationEtTags.text.clear()
            }
        }

        tagRecyclerView = findViewById(R.id.activity_trip_creation_rv_tag_list)
    }
}

// TODO Add TagAdapter class https://developer.android.com/develop/ui/views/layout/recyclerview#kotlin