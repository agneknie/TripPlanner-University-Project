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
        // Example Tags
        private val exampleTag = Tag(0, "Sheffield")
        private val exampleTag2 = Tag(0, "University")
        private val exampleTag3 = Tag(0, "Europe")

        // Example Trip
        private val exampleTrip = Trip(0, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Example Trip: Diamond to Home", 1)

        // Example Locations
        private val exampleLocation1 =
            Location(0,
                53.3811722,
                -1.4812385,
                "", "", LocalDateTime.now(), 1)
        private val exampleLocation2 =
            Location(0,
                53.3816988,
                -1.4816326,
                "", "", LocalDateTime.now(), 1)
        private val exampleLocation3 =
            Location(0,
                53.3819698,
                -1.4816712,
                "", "", LocalDateTime.now(), 1)
        private val exampleLocation4 =
            Location(0,
                53.3822195,
                -1.4816491,
                "", "", LocalDateTime.now(), 1)
        private val exampleLocation5 =
            Location(0,
                53.382506,
                -1.4818619,
                "", "", LocalDateTime.now(), 1)
        private val exampleLocation6 =
            Location(0,
                53.3830355,
                -1.4818304,
                "", "", LocalDateTime.now(), 1)

        /**
         * Seed the database with an example Trip from the Diamond, together
         * with example Tags.
         */
        fun seedWithExamples(tripPlannerViewModel: TripPlannerViewModel){
            seedExampleTags(tripPlannerViewModel)
            seedExampleTrip(tripPlannerViewModel)
        }

        /**
         * Seed database with example Trip with Locations.
         */
        private fun seedExampleTrip(tripPlannerViewModel: TripPlannerViewModel){
            tripPlannerViewModel.insertTrip(exampleTrip)
            seedExampleTripLocations(tripPlannerViewModel)
        }

        /**
         * Seeds the database with example Tags.
         */
        private fun seedExampleTags(tripPlannerViewModel: TripPlannerViewModel){
            tripPlannerViewModel.insertTag(exampleTag)
            tripPlannerViewModel.insertTag(exampleTag2)
            tripPlannerViewModel.insertTag(exampleTag3)
        }

        /**
         * Seeds the database with Locations of an example Trip.
         */
        private fun seedExampleTripLocations(tripPlannerViewModel: TripPlannerViewModel){
            tripPlannerViewModel.insertLocation(exampleLocation1)
            tripPlannerViewModel.insertLocation(exampleLocation2)
            tripPlannerViewModel.insertLocation(exampleLocation3)
            tripPlannerViewModel.insertLocation(exampleLocation4)
            tripPlannerViewModel.insertLocation(exampleLocation5)
            tripPlannerViewModel.insertLocation(exampleLocation6)
        }
    }
}