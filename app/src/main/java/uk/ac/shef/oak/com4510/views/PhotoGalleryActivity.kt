package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_photo_gallery.view.*
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.PhotoGallery
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoGalleryBinding
import uk.ac.shef.oak.com4510.helpers.PhotoSortingOption

/**
 * Class PhotoGalleryActivity.
 *
 * Implements all photo browsing and sorting in a gallery of photos.
 */
class PhotoGalleryActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoGalleryBinding
    private lateinit var photoGallery: PhotoGallery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoGallery = PhotoGallery(this, tripPlannerViewModel)

        initialiseItemSelectListener()
        initialiseSortingDirectionClickListener()

        // TODO Sorting doesn't work
        // TODO Reverse button doesn't work
        // TODO Change reverse button to image
    }

    private fun initialiseItemSelectListener() {
        val sortDropdown = binding.activityPhotoGallerySpSorting

        // Creates array adapter with photo sorting options objects to display
        val arrayAdapter: ArrayAdapter<PhotoSortingOption> = ArrayAdapter<PhotoSortingOption>(
            this,
            android.R.layout.simple_spinner_item, PhotoSortingOption.values()
        )

        // Specifies default layout to view dropdown
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Saves list of choices to the dropdown
        sortDropdown.adapter = arrayAdapter

        // Sets listener on the spinner to enable its functionality
        sortDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Gets selected sorting option
                val selectedSortingOption: PhotoSortingOption? = PhotoSortingOption.stringToPhotoSortingOption(
                    parent.getItemAtPosition(position).toString()
                )

                if(selectedSortingOption != null)
                    photoGallery.changePhotos(selectedSortingOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implementation not used.
            }
        }
    }

    private fun initialiseSortingDirectionClickListener(){
        binding.root.activity_photo_gallery_iv_sort.setOnClickListener {
            photoGallery.reversePhotoOrder()
        }
    }
}