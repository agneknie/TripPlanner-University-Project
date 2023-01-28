package uk.ac.shef.oak.com4510.components.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.models.Tag

/**
 * Class TagAdapter.
 *
 * Provides adapter(ListAdapter) functionality to the Tags RecyclerView.
 */
class TagAdapter(val tagItemClickListener: TagItemClickListener)
    : ListAdapter<Tag, TagAdapter.TagViewHolder>(TagComparator())
{

    lateinit var context: Context

    /**
     * Click listener, which when implemented, allows access to the
     * clicked tag in the implementing activity.
     */
    interface TagItemClickListener{
        /**
         * Gets information about a Tag, which was clicked in the Tag RecyclerView.
         * Marks tag as selected/deselected and saves it for future reference.
         */
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