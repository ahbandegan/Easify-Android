package ir.amirhesambandegan.easify_location

import kotlin.math.*

/**
 * Utility object for geographical and spatial mathematical calculations.
 */
object LocationMath {
    /**
     * Mean radius of the Earth in meters.
     */
    private const val EARTH_RADIUS_METERS = 6371000.0

    /**
     * Calculates the great-circle distance between two geographic coordinates using the Haversine formula.
     *
     * @param lat1 Latitude of the start point in degrees.
     * @param lon1 Longitude of the start point in degrees.
     * @param lat2 Latitude of the destination point in degrees.
     * @param lon2 Longitude of the destination point in degrees.
     * @return The calculated distance in meters between the two points.
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
                
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (EARTH_RADIUS_METERS * c).toFloat()
    }
}
