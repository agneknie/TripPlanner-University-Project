# TripPlanner
![TripPlanner Logo](app/src/main/trip_planner_icon-playstore.png)
# Introduction
TripPlanner is an Android Kotlin application, which provides the user with the trip tracking
functionality. The main app features are outlined in the remainder of this section.

Utilises Google Maps API functionality.

Completed as an assessment on the COM4510 'Mobile Software Development' module at the University
of Sheffield.

## Trip Tracking
Application allows the user to create a trip including marking it with custom tags and a description.
When the trip starts, user's location is tracked every 20 seconds, together with barometric pressure
and temperature if such sensors are available. During the trip, user can take or select photos from
gallery, which are then associated with the last tracked location. The trip also tracks its start and
end times.

## Photo Gallery
Allows the user to browse through all the photos, which were taken or added during their trips. 
The gallery supports photo sorting/search on different parameters in ascending or descending order.
When a photo is selected, displays all of its properties, including location and place taken at
during the trip on the map. Further inspection allows to view original photo in full size.

## Photo Map
Allows the user to observe all photos, which were taken or added during their trips as locations
on the map. Similarly, as in the Photo Gallery, photo location selection and further photo inspection
is possible.

## Past Trips (Extra Feature)
Allows user to view a list of all past trips. Furthermore, allows trip inspection, where the trip
path is displayed on the map, together with a photo gallery of all photos taken during the trip.
Same as before, further photo details can be inspected

# Application Structure
## Activity Diagram
![Activity Diagram](readme_related/Activity%20Diagram.png)
## Database Diagram
![Database Diagram](readme_related/Database%20Diagram.png)