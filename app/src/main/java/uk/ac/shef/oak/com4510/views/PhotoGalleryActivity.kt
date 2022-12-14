package uk.ac.shef.oak.com4510.views

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_photo_gallery.*
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.components.PhotoGallery
import uk.ac.shef.oak.com4510.components.adapters.GalleryPhotoAdapter
import uk.ac.shef.oak.com4510.data.access.daos.PhotoDao
import uk.ac.shef.oak.com4510.databinding.ActivityPhotoGalleryBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel

/**
 * Class PhotoGalleryActivity.
 *
 * Implements all photo browsing and sorting in a gallery of photos.
 */
class PhotoGalleryActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityPhotoGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PhotoGallery(this, tripPlannerViewModel)

        initialiseItemSelectListener()
        initialiseClickListener()

    }

    private fun initialiseItemSelectListener() {
        binding.activityPhotoGallerySpSorting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedPhotoAttribute = parent?.getString(p2)
                if (selectedPhotoAttribute == "ID"){
                    tripPlannerViewModel.allPhotos
                }
                if (selectedPhotoAttribute == "Location"){
                    tripPlannerViewModel.allPhotosByLocation
                }
                if (selectedPhotoAttribute == "Tag"){
                    tripPlannerViewModel.allPhotosByTag
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }
        }
    }

    private fun initialiseClickListener(){
        binding.activityPhotoGalleryIbSorting.setOnClickListener {
            //GalleryPhotoAdapter.reverse()
        }
    }
}