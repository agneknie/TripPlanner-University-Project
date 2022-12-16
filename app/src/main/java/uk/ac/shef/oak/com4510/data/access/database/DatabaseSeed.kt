package uk.ac.shef.oak.com4510.data.access.database

import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.models.Tag
import uk.ac.shef.oak.com4510.models.Trip
import uk.ac.shef.oak.com4510.viewmodels.TripPlannerViewModel
import java.time.LocalDateTime

/**
 * Class DatabaseSeed.
 *
 * Seeds the database with an example trip.
 */
class DatabaseSeed {
    companion object{
        fun seedExampleTrip(tripPlannerViewModel: TripPlannerViewModel){
            var exampleTag = Tag(0, "Sheffield")
            var exampleTag2 = Tag(0, "University")
            var exampleTag3 = Tag(0, "Europe")
            var exampleTrip = Trip(0, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Example Trip: Diamond to Home", 1)
            var exampleLocation1 =
                Location(0,
                    53.3811722,
                    -1.4812385,
                    "", "", LocalDateTime.now(), 1)
            var exampleLocation2 =
                Location(0,
                    53.3816988,
                    -1.4816326,
                    "", "", LocalDateTime.now(), 1)
            var exampleLocation3 =
                Location(0,
                    53.3819698,
                    -1.4816712,
                    "", "", LocalDateTime.now(), 1)
            var exampleLocation4 =
                Location(0,
                    53.3822195,
                    -1.4816491,
                    "", "", LocalDateTime.now(), 1)
            var exampleLocation5 =
                Location(0,
                    53.382506,
                    -1.4818619,
                    "", "", LocalDateTime.now(), 1)
            var exampleLocation6 =
                Location(0,
                    53.3830355,
                    -1.4818304,
                    "", "", LocalDateTime.now(), 1)

            tripPlannerViewModel.insertTag(exampleTag)
            tripPlannerViewModel.insertTag(exampleTag2)
            tripPlannerViewModel.insertTag(exampleTag3)

            tripPlannerViewModel.insertTrip(exampleTrip)

            tripPlannerViewModel.insertLocation(exampleLocation1)
            tripPlannerViewModel.insertLocation(exampleLocation2)
            tripPlannerViewModel.insertLocation(exampleLocation3)
            tripPlannerViewModel.insertLocation(exampleLocation4)
            tripPlannerViewModel.insertLocation(exampleLocation5)
            tripPlannerViewModel.insertLocation(exampleLocation6)
        }
    }
}