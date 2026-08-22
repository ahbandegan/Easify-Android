package ir.amirhesambandegan.easify_location

import android.location.Location
import kotlin.math.roundToInt

/**
 * Calculates the device's movement speed in kilometers per hour (km/h).
 *
 * Converts internal speed (meters per second) to km/h by multiplying by 3.6.
 *
 * @receiver The [Location] instance containing speed information.
 * @return The speed in km/h, or `0f` if [Location.hasSpeed] returns `false`.
 */
fun Location.getSpeedKmh(): Float {
    if (!hasSpeed()) return 0f
    // Speed is in meters/second
    return this.speed * 3.6f
}

/**
 * Calculates the Estimated Time of Arrival (ETA) in minutes from the current location to a destination.
 *
 * Uses the current speed via [getSpeedKmh] and the straight-line distance calculated via [LocationMath.calculateDistance].
 * If the current speed is less than 5 km/h, calculation returns `null` to avoid inaccurate or infinite estimations.
 *
 * @receiver The current [Location].
 * @param destinationLat Latitude of the destination point in degrees.
 * @param destinationLng Longitude of the destination point in degrees.
 * @return Estimated time of arrival in minutes rounded to the nearest integer, or `null` if the current speed is below 5 km/h.
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
