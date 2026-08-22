package ir.amirhesambandegan.easify_location

import kotlin.math.*

object LocationMath {
    private const val EARTH_RADIUS_METERS = 6371000.0

    /**
     * Calculates the distance between two coordinates using the Haversine formula.
     * Returns distance in meters.
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
