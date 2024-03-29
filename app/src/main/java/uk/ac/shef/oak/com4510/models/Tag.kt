package uk.ac.shef.oak.com4510.models

/**
 * Class Tag.
 *
 * Defines the Tag model.
 */
data class Tag(
    val tagId: Int = 0,
    val tagName: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        if (tagId != other.tagId) return false
        if (tagName != other.tagName) return false

        return true
    }
}