package ir.amirhesambandegan.easify_location

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Pure multiplatform geographic coordinate model usable in Console, KMP, and Android modules.
 */
data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float? = null,
    val speedMps: Float? = null,
    val timestampMillis: Long = 0L
)

/**
 * Pure multiplatform geographic and spatial math utilities.
 */
object LocationMath {
    private const val EARTH_RADIUS_METERS = 6371000.0

    private fun Double.toRadians(): Double = this * (PI / 180.0)
    private fun Double.toDegrees(): Double = this * (180.0 / PI)

    /** Calculates great-circle Haversine distance between two coordinates in meters. */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val dLat = (lat2 - lat1).toRadians()
        val dLon = (lon2 - lon1).toRadians()

        val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(lat1.toRadians()) * cos(lat2.toRadians()) *
            sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt((1.0 - a).coerceAtLeast(0.0)))
        return (EARTH_RADIUS_METERS * c).toFloat()
    }

    /** Calculates Haversine distance in meters between two [GeoPoint]s. */
    fun distanceBetween(a: GeoPoint, b: GeoPoint): Float =
        calculateDistance(a.latitude, a.longitude, b.latitude, b.longitude)

    /** Calculates initial forward azimuth bearing in degrees (`0.0..360.0`) from point 1 to point 2. */
    fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val phi1 = lat1.toRadians()
        val phi2 = lat2.toRadians()
        val dLon = (lon2 - lon1).toRadians()
        val y = sin(dLon) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(dLon)
        val theta = atan2(y, x).toDegrees()
        return (((theta % 360.0) + 360.0) % 360.0).toFloat()
    }

    /** Checks whether ([lat], [lng]) falls within [radiusMeters] of ([centerLat], [centerLng]). */
    fun isWithinRadius(
        lat: Double,
        lng: Double,
        centerLat: Double,
        centerLng: Double,
        radiusMeters: Float
    ): Boolean = calculateDistance(lat, lng, centerLat, centerLng) <= radiusMeters
}

fun formatGoogleMapsLink(latitude: Double, longitude: Double): String =
    "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

fun formatCoordinatesDms(latitude: Double, longitude: Double): String {
    fun component(coord: Double, isLatitude: Boolean): String {
        val absCoord = abs(coord)
        val degrees = absCoord.toInt()
        val minutesDouble = (absCoord - degrees) * 60.0
        val minutes = minutesDouble.toInt()
        val seconds = ((minutesDouble - minutes) * 60.0).toInt()
        val dir = if (isLatitude) {
            if (coord >= 0.0) "N" else "S"
        } else {
            if (coord >= 0.0) "E" else "W"
        }
        return "$degrees° $minutes' $seconds\" $dir"
    }
    return "${component(latitude, true)}, ${component(longitude, false)}"
}

fun GeoPoint.toGoogleMapsLink(): String = formatGoogleMapsLink(latitude, longitude)

fun GeoPoint.toDMSFormat(): String = formatCoordinatesDms(latitude, longitude)

fun GeoPoint.getSpeedKmh(): Float {
    val mps = speedMps ?: return 0f
    return if (mps <= 0f) 0f else mps * 3.6f
}

fun GeoPoint.calculateETA(destinationLat: Double, destinationLng: Double): Int? {
    val speedKmh = getSpeedKmh()
    if (speedKmh < 5f) return null
    val distanceKm = LocationMath.calculateDistance(latitude, longitude, destinationLat, destinationLng) / 1000f
    val timeHours = distanceKm / speedKmh
    return (timeHours * 60f).roundToInt()
}
