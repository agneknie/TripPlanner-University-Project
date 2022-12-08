package uk.ac.shef.oak.com4510.utilities

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Size
import androidx.core.net.toUri
import uk.ac.shef.oak.com4510.models.Photo
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * PhotoUtilities Class.
 *
 * Handles photo thumbnail creation and/or retrieval and saves it to cache if
 * not already saved.
 */
class PhotoUtilities {
    companion object{
        private const val THUMBNAIL_WIDTH = 250
        private const val THUMBNAIL_HEIGHT = 250
        private const val BITMAP_QUALITY = 0    // Range from 0 to 100

        private const val THUMBNAIL_PREFIX = "TripPlanner_"
        private const val THUMBNAIL_SUFFIX = ".jpg"

        /**
         * Retrieves or makes a thumbnail and returns its path as a string.
         *
         * Used when thumbnail exists.
         */
        fun getOrMakeThumbnail(context: Context, photo: Photo): String {
            val thumbnailStringPath = photo.thumbnailPath?.toString() ?: String()
            return getOrMakeThumbNail(context, thumbnailStringPath, photo.photoPath.toString())
        }

        /**
         * Returns String, which is an URI, which points to a thumbnail file if it exits.
         * Otherwise, creates it before returning the URI as String object.
         *
         * Used when it is not sure if the thumbnail exists or not.
         */
        fun getOrMakeThumbNail(context: Context, thumbnailPath: String, photoPath: String): String {
            return if(thumbnailPath.isBlank() || !File(context.cacheDir, thumbnailPath).exists()){
                getOrMakeThumbNail(context, photoPath)
            } else thumbnailPath
        }

        /**
         * Returns String, which is an URI, which points to a thumbnail file if it exits.
         * Otherwise, creates it before returning the URI as String object.
         *
         * Used when thumbnail does not exist for sure.
         */
        fun getOrMakeThumbNail(context: Context, photoPath: String): String {
            var thumbnailUriString: String

            Uri.parse(photoPath).let {
                val thumbnailBitmap =
                    context.contentResolver.loadThumbnail(it, Size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT), null)
                thumbnailUriString = saveBitmapToCache(thumbnailBitmap, context)
            }

            return thumbnailUriString
        }

        /**
         * Saves a bitmap of the photo thumbnail to cache.
         */
        private fun saveBitmapToCache(bitmap: Bitmap, context: Context): String{
            var thumbnailCacheFile: File?

            bitmap.let{
                // Converts bitmap to byte array
                val bos = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bos)
                val bitmapByteArray = bos.toByteArray()

                // Saves the byte array to file in the file cache directory
                thumbnailCacheFile = File.createTempFile(THUMBNAIL_PREFIX, THUMBNAIL_SUFFIX, context.cacheDir)
                val fos = FileOutputStream(thumbnailCacheFile)
                fos.write(bitmapByteArray)
                fos.flush()
                fos.close()
            }

            return thumbnailCacheFile?.toUri().toString()
        }
    }
}