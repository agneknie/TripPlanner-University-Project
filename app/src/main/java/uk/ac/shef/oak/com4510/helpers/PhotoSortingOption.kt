package uk.ac.shef.oak.com4510.helpers

/**
 * Enum PhotoSortingOption.
 *
 * Describes a Photo Sorting Option.
 */
enum class PhotoSortingOption(val type: String){
    DEFAULT("Default"),
    LOCATION("Location"),
    TAG("Tag"),
    TITLE("Title"),
    DESCRIPTION("Description");

    override fun toString(): String {
        return this.type
    }

    companion object{
        /**
         * Converts a string representing PhotoSortingOption to a PhotoSortingOption.
         */
        fun stringToPhotoSortingOption(photoSortingOptionString: String): PhotoSortingOption? {
            for (taskSortingOption in PhotoSortingOption.values()) {
                if (photoSortingOptionString == taskSortingOption.type) return taskSortingOption
            }
            return null
        }
    }
}