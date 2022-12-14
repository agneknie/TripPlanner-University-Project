package uk.ac.shef.oak.com4510

import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModelFactory

/**
 * Class TripPlannerAppCompatActivity.
 *
 * Instantiates ViewModels of the application.
 * Contains convenience methods, to be used by all inheriting activities
 */
open class TripPlannerAppCompatActivity: AppCompatActivity() {

    // Initialise ViewModel
    protected val tripPlannerViewModel: TripPlannerViewModel by viewModels {
        TripPlannerViewModelFactory(
            (application as TripPlannerApplication).tripRepository,
            (application as TripPlannerApplication).locationRepository,
            (application as TripPlannerApplication).photoRepository,
            (application as TripPlannerApplication).tagRepository,
            application)
    }

    /**
     * Gets the TripPlannerViewModel.
     * Intended for service access use.
     */
    fun getViewModel(): TripPlannerViewModel{
        return tripPlannerViewModel
    }

    /**
     * Convenience method for displaying a Snackbar.
     * Uses resource id to locate a string.
     */
    fun displaySnackbar(view: View, messageStringId: Int){
        val snackbar = Snackbar.make(view, messageStringId, Snackbar.LENGTH_LONG)
        configureAndShowSnackbar(snackbar)
    }

    /**
     * Convenience method for displaying a Snackbar.
     * Uses string and just displays it.
     */
    fun displaySnackbar(view: View, messageString: String){
        val snackbar = Snackbar.make(view, messageString, Snackbar.LENGTH_LONG)
        configureAndShowSnackbar(snackbar)
    }

    /**
     * Sets snackbar's colour and shows it.
     */
    private fun configureAndShowSnackbar(snackbar: Snackbar){
        snackbar.setBackgroundTint(this.getColor(R.color.main_colour))
        snackbar.setTextColor(this.getColor(R.color.app_background))

        snackbar.show()
    }
}