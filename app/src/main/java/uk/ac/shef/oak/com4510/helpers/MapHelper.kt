package uk.ac.shef.oak.com4510.helpers

import android.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import uk.ac.shef.oak.com4510.models.Location

/**
 * Class MapHelper.
 *
 * Provides convenience methods for drawing map markers,
 * paths between them and map camera zooming.
 */
class MapHelper {
    companion object{
        // Default map zoom
        const val MAP_ZOOM = 17.0f

        // Map line between points default width
        const val MAP_LINE_WIDTH = 5F

        // Map line between points default colour
        const val MAP_LINE_COLOUR = Color.RED

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
                    MAP_ZOOM
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
                    .width(MAP_LINE_WIDTH)
                    .color(MAP_LINE_COLOUR)
                    .geodesic(true) // to make the line curve
            )
        }
    }
}