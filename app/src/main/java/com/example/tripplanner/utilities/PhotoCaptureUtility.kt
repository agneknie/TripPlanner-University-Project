package com.example.tripplanner.utilities

import com.example.tripplanner.R

/**
 * Class for all utilities related with photo capturing.
 */
class PhotoCaptureUtility {
    companion object{
        private const val PHOTO_TAG = R.string.app_name.toString()

        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_FORMAT = "image/jpeg"
        const val PHOTO_RELATIVE_PATH = "Pictures/${PHOTO_TAG}"
    }
}