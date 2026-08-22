package ir.amirhesambandegan.easify_location

import android.location.Location
import kotlin.math.roundToInt

/**
 * Returns the speed in Kilometers Per Hour.
 */
fun Location.getSpeedKmh(): Float {
    if (!hasSpeed()) return 0f
    // Speed is in meters/second
    return this.speed * 3.6f
}

/**
 * Calculates Estimated Time of Arrival (ETA) in minutes.
 * @param destinationLat Latitude of the destination.
 * @param destinationLng Longitude of the destination.
 * @return ETA in minutes, or null if speed is too slow to estimate.
 */
fun Location.calculateETA(destinationLat: Double, destinationLng: Double): Int? {
    val speedKmh = getSpeedKmh()
    if (speedKmh < 5f) return null // Too slow, can't estimate accurately
    
    val distanceMeters = LocationMath.calculateDistance(
        this.latitude, this.longitude, destinationLat, destinationLng
    )
    val distanceKm = distanceMeters / 1000f
    
    val timeHours = distanceKm / speedKmh
    return (timeHours * 60).roundToInt()
}
