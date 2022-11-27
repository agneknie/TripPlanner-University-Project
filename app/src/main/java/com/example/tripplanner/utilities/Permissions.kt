package com.example.tripplanner.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tripplanner.R
import com.example.tripplanner.TripPlannerAppCompatActivity
import com.google.android.material.snackbar.Snackbar

/**
 * Deals with permissions for the application. To be used in the launch activity.
 *
 * // TODO For each additional permission:
 * // TODO 1. Add to manifest; 2. Add to REQUIRED_PERMISSIONS;
 * // TODO 3. Add canX method, to act accordingly in an activity, where permission is not granted.
 */
class Permissions {
    companion object{
        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ).apply {
                // If newer Android version is used
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }.toTypedArray()

        /**
         * Returns true, if camera can be used.
         */
        fun canUseCamera(activity: Activity): Boolean{
            return if(activity.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                PackageManager.PERMISSION_GRANTED ==
                        activity.checkSelfPermission(Manifest.permission.CAMERA)
            } else false
        }

        /**
         * Returns true, if storage can be accessed.
         */
        fun canModifyStorage(activity: Activity): Boolean{
            val permissionGranted = PackageManager.PERMISSION_GRANTED ==
                    activity.checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION)

            // If newer Android version is used
            var additionalPermissions = true
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                additionalPermissions = PackageManager.PERMISSION_GRANTED ==
                        activity.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
            }

            return permissionGranted && additionalPermissions
        }

        /**
         * Function to be called in onCreate method of launch activity,
         * to assure all necessary permissions are granted.
         */
        fun checkAndRequestPermissions(activity: Activity){
            if (!allPermissionsGranted(activity)) {
                ActivityCompat.requestPermissions(
                    activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }

        /**
         * Function to override request permissions callback by the
         * requesting app.
         */
        fun onRequestPermissionsCallback(
            activity: TripPlannerAppCompatActivity,
            rootView: View,
            requestCode: Int){
            if(requestCode == REQUEST_CODE_PERMISSIONS){
                if(allPermissionsGranted(activity)){
                    activity.displaySnackbar(rootView, R.string.permissions_granted_snackbar, Snackbar.LENGTH_LONG)
                }
                else{
                    activity.displaySnackbar(rootView, R.string.permissions_not_granted_snackbar, Snackbar.LENGTH_LONG)
                }
            }
        }

        /**
         * Checks if all permissions that are necessary are granted.
         */
        private fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}