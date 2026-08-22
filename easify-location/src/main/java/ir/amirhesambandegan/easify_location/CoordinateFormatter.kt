package ir.amirhesambandegan.easify_location

import android.location.Location
import kotlin.math.abs

/**
 * Generates a ready-to-share Google Maps link.
 */
fun Location.toGoogleMapsLink(): String {
    return "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
}

/**
 * Formats coordinates into Degrees, Minutes, Seconds (e.g., 35° 43' 12" N).
 */
fun Location.toDMSFormat(): String {
    val latDms = convertToDms(latitude, isLatitude = true)
    val lngDms = convertToDms(longitude, isLatitude = false)
    return "$latDms, $lngDms"
}

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
