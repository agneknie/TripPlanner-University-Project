package uk.ac.shef.oak.com4510.helpers

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import uk.ac.shef.oak.com4510.models.Location
import uk.ac.shef.oak.com4510.utilities.ServicesUtilities

/**
 * Class MapHelper.
 *
 * Provides convenience methods for drawing map markers,
 * paths between them and map camera zooming.
 */
class MapHelper {
    companion object{
        /**
         * Adds location marker for the given location
         */
        fun addLocationMarker(map: GoogleMap, location: Location, defaultColour: Boolean){
            var markerOptions = MarkerOptions()
                .position(LatLng(location.xCoordinate, location.yCoordinate))
                .title(location.getLocationMarkerTitle())

            if(!defaultColour){
                markerOptions.icon(
                    (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                )
            }
            map.addMarker(markerOptions)
        }

        /**
         * Moves map camera to the provided location.
         */
        fun moveCameraToLocation(map: GoogleMap, tripLocation: Location){
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(tripLocation.xCoordinate, tripLocation.yCoordinate),
                    ServicesUtilities.MAP_ZOOM
                )
            )
        }

        /**
         * Draws a line/path on the map between two given locations.
         */
        fun drawLineBetweenLocations(map: GoogleMap, locationOne: Location, locationTwo: Location){
            map.addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .add(LatLng(locationOne.xCoordinate, locationOne.yCoordinate), LatLng(locationTwo.xCoordinate, locationTwo.yCoordinate))
                    .width(ServicesUtilities.MAP_LINE_WIDTH)
                    .color(ServicesUtilities.MAP_LINE_COLOUR)
                    .geodesic(true) // to make the line curve
            )
        }
    }
}