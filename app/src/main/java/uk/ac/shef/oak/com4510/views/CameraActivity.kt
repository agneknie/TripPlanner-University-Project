package uk.ac.shef.oak.com4510.views

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.TripPlannerAppCompatActivity
import uk.ac.shef.oak.com4510.databinding.ActivityCameraBinding
import uk.ac.shef.oak.com4510.utilities.IntentKeys
import uk.ac.shef.oak.com4510.utilities.PhotoUtilities
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class CameraActivity.
 *
 * Corresponds to the Camera view. Starts the camera for a user to take a photo.
 */
class CameraActivity: TripPlannerAppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var photoCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start camera
        startCamera()

        // Setup camera button listener
        configureCameraCaptureButton()
    }

    /**
     * Assigns listener to the camera capture button.
     */
    private fun configureCameraCaptureButton(){
        binding.activityCameraBtnPhotoCapture.setOnClickListener {
            takePhoto()
        }
    }

    /**
     * Captures a photo, when user clicks the camera capture button.
     */
    private fun takePhoto(){
        val photoCapture = photoCapture?: return

        // Create name for captured photo
        val photoName = SimpleDateFormat(PhotoUtilities.FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis())

        // Set media content values
        val mediaContentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, photoName)
            put(MediaStore.MediaColumns.MIME_TYPE, PhotoUtilities.PHOTO_FORMAT)
            put(MediaStore.Images.Media.RELATIVE_PATH, PhotoUtilities.PHOTO_RELATIVE_PATH)
        }

        // Create output options
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                mediaContentValues)
            .build()

        // Configure ImageCapture listener, triggered after photo capture
        photoCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onError(exception: ImageCaptureException) {
                    displaySnackbar(binding.root, R.string.photo_capture_failed_snackbar)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){
                    val intent = Intent(this@CameraActivity, TripPhotoActivity::class.java)
                    intent.putExtra(IntentKeys.PHOTO_URI, output.savedUri.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        )
    }

    /**
     * Configures & starts the camera.
     */
    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Bind lifecycle of camera to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Build preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.activityCameraVfView.surfaceProvider)
                }

            // Build ImageCapture & initialise global variable
            photoCapture = ImageCapture.Builder().build()

            // Select back camera as default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Start the camera
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, photoCapture)
            } catch (exc: Exception){
                displaySnackbar(binding.root, R.string.camera_initialisation_failed_snackbar)
            }

        }, ContextCompat.getMainExecutor(this))
    }
}