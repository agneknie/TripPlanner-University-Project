package com.example.tripplanner.components

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityTripCreationBinding
import com.example.tripplanner.models.Tag
import com.example.tripplanner.viewmodels.TripPlannerViewModel
import kotlinx.android.synthetic.main.tags_panel.view.*

/**
 * Handles initialisation and behaviour of the tags addition and
 * display panel.
 *
 * - Changes tag's colour when selected/deselected;
 * - Displays tags in the database;
 * - Implements TagAdapter.TagItemClickListener interface, so that
 *      currently selected tag can be accessed in the main activity.
 */
class TagsPanel(
    private val invokingActivity: TripPlannerAppCompatActivity,
    private val binding: ActivityTripCreationBinding,
    private val tripPlannerViewModel: TripPlannerViewModel,
    private var selectedTag: Tag? = null,
    private var selectedTagView: View? = null)
    : TagAdapter.TagItemClickListener {

    // Tag related variables
    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var tagAdapter: TagAdapter

    init {
        setupNewTagClickListener()
        setupTagRecyclerView()
    }

    /**
     * Returns selected Tag to the activity.
     */
    fun getSelectedTag(): Tag?{
        return selectedTag
    }

    /**
     * Click listener for "Add New Tag" button, which inserts a new
     * tag in the database.
     */
    private fun setupNewTagClickListener(){
        binding.activityTripCreationLlTagsPanel.tags_panel_btn_add_tag.setOnClickListener{
            val tagNameString = binding.activityTripCreationLlTagsPanel.tags_panel_creation_et_tags.text.toString()
            if(tagNameString.isNotEmpty()){
                val newTag = Tag(0,tagNameString)
                tripPlannerViewModel.insertTag(newTag)
                binding.activityTripCreationLlTagsPanel.tags_panel_creation_et_tags.text.clear()
            }
        }
    }

    /**
     * Tag RecyclerView setup. Observes Tag data in the database and
     * displays in the RecyclerView.
     */
    private fun setupTagRecyclerView(){
        tagRecyclerView = invokingActivity.findViewById(R.id.tags_panel_creation_rv_tag_list)
        tagAdapter = TagAdapter(this)
        tagRecyclerView.adapter = tagAdapter
        tagRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        tripPlannerViewModel.allTags.observe(invokingActivity){
            it?.let{
                tagAdapter.submitList(it)
            }
        }
    }

    /**
     * Resets Tag's appearance to default colours.
     */
    private fun resetTagColour(tagView: View){
        tagView.background = AppCompatResources.getDrawable(invokingActivity, R.drawable.tag_view_background)
        (tagView as TextView).setTextColor(ContextCompat.getColor(invokingActivity, R.color.white))
    }

    /**
     * Resets Tag's appearance to default colours.
     */
    private fun displayTagAsSelected(tagView: View){
        tagView.background = AppCompatResources.getDrawable(invokingActivity, R.drawable.tag_view_background_selected)
        (tagView as TextView).setTextColor(ContextCompat.getColor(invokingActivity, R.color.main_colour))
    }

    override fun onTagItemClick(tag: Tag, tagView: View) {
        // If already selected tag was clicked
        if(tag.equals(selectedTag)){
            resetTagColour(tagView)

            selectedTagView = null
            selectedTag = null
        }

        // If not selected tag was clicked
        else{
            // Changes previously selected tag to default state
            if(selectedTag!= null && selectedTagView != null){
                resetTagColour(selectedTagView!!)
            }

            // Changes selected tag's colour
            displayTagAsSelected(tagView)

            // Saves newly selected tag
            selectedTagView = tagView
            selectedTag = tag
        }
    }
}