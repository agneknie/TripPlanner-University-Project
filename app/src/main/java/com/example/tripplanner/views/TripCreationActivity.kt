package com.example.tripplanner.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.example.tripplanner.databinding.ActivityTripCreationBinding
import com.example.tripplanner.models.Tag

class TripCreationActivity: TripPlannerAppCompatActivity(), TagAdapter.TagItemClickListener{
    private lateinit var binding: ActivityTripCreationBinding

    // Tag related variables
    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var tagAdapter: TagAdapter
    private var selectedTag: Tag? = null
    private var selectedTagView: View? = null

    override fun onCreate(savedInstanceState: Bundle?){
        // Activity binding & layout configuration
        super.onCreate(savedInstanceState)
        binding = ActivityTripCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Click listener for "Add New Tag" button, which inserts a new tag in the database
        binding.activityTripCreationBtnAddTag.setOnClickListener{
            val tagNameString = binding.activityTripCreationEtTags.text.toString()
            if(tagNameString.isNotEmpty()){
                val newTag = Tag(0,tagNameString)
                tripPlannerViewModel.insertTag(newTag)
                binding.activityTripCreationEtTags.text.clear()
            }
        }

        // Tag RecyclerView setup
        tagRecyclerView = findViewById(R.id.activity_trip_creation_rv_tag_list)
        tagAdapter = TagAdapter(this)
        tagRecyclerView.adapter = tagAdapter
        tagRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        // Observing Tags from ViewModel
        tripPlannerViewModel.allTags.observe(this){
            it?.let{
                tagAdapter.submitList(it)
            }
        }
    }

    /**
     * Gets information about a Tag, which was clicked in the Tag RecyclerView.
     * Marks tag as selected/deselected and saves it for future reference.
     */
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

    private fun resetTagColour(tagView: View){
        tagView.background = AppCompatResources.getDrawable(this, R.drawable.tag_view_background)
        (selectedTagView as TextView).setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun displayTagAsSelected(tagView: View){
        tagView.background = AppCompatResources.getDrawable(this, R.drawable.tag_view_background_selected)
        (tagView as TextView).setTextColor(ContextCompat.getColor(this, R.color.main_colour))
    }
}

/**
 * TagAdapter class, which provides adapter(ListAdapter)
 * functionality to the Tags RecyclerView.
 */
class TagAdapter(val tagItemClickListener: TagItemClickListener): ListAdapter<Tag, TagAdapter.TagViewHolder>(TagComparator()){
    lateinit var context: Context

    interface TagItemClickListener{
        fun onTagItemClick(tag: Tag, tagView: View)
    }

    /**
     * Provides a reference to the type of views that tags are using.
     */
    inner class TagViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tagView: TextView = view.findViewById(R.id.tag_view_tv_tag_name)

        /**
         * Initialises tagView and configures its onClickListener.
         */
        fun bind(tag: Tag){
            tagView.text = tag.tagName
            tagView.setOnClickListener{
                // Notifies that tag was selected
                tagItemClickListener.onTagItemClick(tag, tagView)
            }
        }
    }

    /**
     * Comparator for Tags, required by ListAdapter.
     */
    class TagComparator: DiffUtil.ItemCallback<Tag>(){
        override fun areItemsTheSame(oldTag: Tag, newTag: Tag): Boolean {
            return oldTag.tagId === newTag.tagId
        }

        override fun areContentsTheSame(oldTag: Tag, newTag: Tag): Boolean {
            return oldTag.equals(newTag)
        }

    }

    /**
     * Creates the tagView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.tag_view,
            parent,
            false
        )
        return TagViewHolder(view)
    }

    /**
     * Replaces contents of the tagView.
     */
    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}