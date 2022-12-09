package uk.ac.shef.oak.com4510.data.access.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import uk.ac.shef.oak.com4510.data.access.daos.TagDao
import uk.ac.shef.oak.com4510.data.access.entities.TagEntity
import uk.ac.shef.oak.com4510.models.Tag

/**
 * Class TagRepository.
 *
 * Implements the repository for the Tag model, matching the queries in
 * the TagDao.
 */
class TagRepository(private val tagDao: TagDao) {

    // Observable for all tags
    val tags: LiveData<List<TagEntity>> = tagDao.getAllTags().asLiveData()

    /**
     * Insert a Tag in the database.
     */
    suspend fun insertTag(tag: Tag){
        tagDao.insertTag(tag.asDatabaseEntity())
    }

    /**
     * Get Tag from the database by its tag_id.
     */
    fun getTag(tagId: Int) = tagDao.getTag(tagId).asLiveData()

}

//region Object Mapping
/**
 * Maps TagEntity to Tag.
 */
fun TagEntity.asDomainModel(): Tag {
    return Tag(
        tagId = tagId,
        tagName = tagName
    )
}

/**
 * Maps List of TagEntity to Tag.
 */
fun List<TagEntity>.asDomainModel(): List<Tag>{
    return map{
        it.asDomainModel()
    }
}

/**
 * Maps Tag to TagEntity.
 */
fun Tag.asDatabaseEntity(): TagEntity {
    return TagEntity(
        tagId = tagId,
        tagName = tagName
    )
}

/**
 * Maps List of Tag to List of TagEntity.
 */
fun List<Tag>.asDatabaseEntities(): List<TagEntity>{
    return map{
        it.asDatabaseEntity()
    }
}
//endregion