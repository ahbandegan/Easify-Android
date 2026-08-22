package ir.amirhesambandegan.easify_location

import android.location.Location
import kotlin.math.abs

/**
 * Generates a ready-to-share Google Maps search URL for the given [Location].
 *
 * @receiver The [Location] containing the latitude and longitude coordinates.
 * @return A formatted URL string pointing to Google Maps search query with latitude and longitude.
 */
fun Location.toGoogleMapsLink(): String {
    return "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
}

/**
 * Formats coordinates into Degrees, Minutes, Seconds (DMS) representation with cardinal directions.
 *
 * For example: `35° 43' 12" N, 51° 24' 36" E`.
 *
 * @receiver The [Location] to format.
 * @return A human-readable DMS string containing latitude and longitude.
 */
fun Location.toDMSFormat(): String {
    val latDms = convertToDms(latitude, isLatitude = true)
    val lngDms = convertToDms(longitude, isLatitude = false)
    return "$latDms, $lngDms"
}

/**
 * Converts a decimal coordinate value into a Degrees, Minutes, Seconds (DMS) string with direction.
 *
 * @param coordinate The decimal coordinate (latitude or longitude).
 * @param isLatitude `true` if formatting latitude (N/S), `false` for longitude (E/W).
 * @return The formatted DMS string component (e.g., `35° 43' 12" N`).
 */
private fun convertToDms(coordinate: Double, isLatitude: Boolean): String {
    val absCoord = abs(coordinate)
    val degrees = absCoord.toInt()
    val minutesDouble = (absCoord - degrees) * 60
    val minutes = minutesDouble.toInt()
    val seconds = ((minutesDouble - minutes) * 60).toInt()
    
    val direction = if (isLatitude) {
        if (coordinate >= 0) "N" else "S"
    } else {
        if (coordinate >= 0) "E" else "W"
    }
    
    return "$degrees° $minutes' $seconds\" $direction"
}
