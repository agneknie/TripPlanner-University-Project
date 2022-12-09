package uk.ac.shef.oak.com4510.components

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import kotlinx.android.synthetic.main.tag_view.view.*
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.models.Tag
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import kotlinx.android.synthetic.main.tags_panel.view.*

/**
 * Class TagsPanel.
 *
 * Handles initialisation and behaviour of the tags addition and
 * display panel.
 *
 * - Changes tag's colour when selected/deselected;
 * - Displays tags in the database;
 * - Implements TagAdapter.TagItemClickListener interface, so that
 *      currently selected tag can be accessed in the main activity.
 */
class TagsPanel(
    private val invokingActivity: uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity,
    private val binding: ViewBinding,
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
        binding.root.tags_panel_btn_add_tag.setOnClickListener{
            val tagNameString = binding.root.tags_panel_creation_et_tags.text.toString()
            if(tagNameString.isNotEmpty()){
                val newTag = Tag(0,tagNameString)
                tripPlannerViewModel.insertTag(newTag)
                binding.root.tags_panel_creation_et_tags.text.clear()
            }
        }
    }

    /**
     * Tag RecyclerView setup. Observes Tag data in the database and
     * displays in the RecyclerView.
     */
    private fun setupTagRecyclerView(){
        val NUMBER_OF_COLUMNS = 3

        tagRecyclerView = invokingActivity.findViewById(R.id.tags_panel_creation_rv_tag_list)
        tagAdapter = TagAdapter(this)
        tagRecyclerView.adapter = tagAdapter
        tagRecyclerView.layoutManager = StaggeredGridLayoutManager(NUMBER_OF_COLUMNS, StaggeredGridLayoutManager.VERTICAL)

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
     * Sets Tag's appearance to selected colours.
     */
    private fun displayTagAsSelected(tagView: View){
        tagView.background = AppCompatResources.getDrawable(invokingActivity, R.drawable.tag_view_background_selected)
        (tagView as TextView).setTextColor(ContextCompat.getColor(invokingActivity, R.color.main_colour))
    }

    /**
     * Sets Tag's appearance to selected colours. Checks if the provided tag
     * id is not null and applies appearance changes only if it is in the
     * database.
     */
    fun displayTagAsSelected(tagId: Int?){
        if(tagId != null){
            tripPlannerViewModel.getTag(tagId).observe(invokingActivity){
                val tagPosition = tagAdapter.currentList.indexOf(it)
                val tagView = tagRecyclerView.getChildAt(tagPosition).tag_view_tv_tag_name

                displayTagAsSelected(tagView)

                selectedTag = it
                selectedTagView = tagView
            }
        }
    }

    /**
     * Handles behaviour, which happens when a tag is clicked.
     * Either selects a tag, deselects currently selected tag and selects a new one
     * or deselects a tag.
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
}