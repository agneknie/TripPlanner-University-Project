package uk.ac.shef.oak.com4510.helpers

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
        fun stringToPhotoSortingOption(photoSortingOptionString: String): PhotoSortingOption? {
            for (taskSortingOption in PhotoSortingOption.values()) {
                if (photoSortingOptionString == taskSortingOption.type) return taskSortingOption
            }
            return null
        }
    }
}