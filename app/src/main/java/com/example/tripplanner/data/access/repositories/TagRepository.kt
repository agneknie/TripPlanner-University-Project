package com.example.tripplanner.data.access.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tripplanner.data.access.daos.TagDao
import com.example.tripplanner.data.access.entities.TagEntity
import com.example.tripplanner.models.Tag

class TagRepository(private val tagDao: TagDao) {

    // Observable for all tags
    val tags: LiveData<List<TagEntity>> = tagDao.getAllTags().asLiveData()

    /**
     * Insert a Tag in the database.
     */
    suspend fun insertTag(tag: Tag){
        tagDao.insertTag(tag.asDatabaseEntity())
    }

    // TODO Add other necessary functions based on Dao
}

//region Object Mapping
/**
 * Maps TagEntity to Tag.
 */
fun TagEntity.asDomainModel(): Tag{
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
fun Tag.asDatabaseEntity(): TagEntity{
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