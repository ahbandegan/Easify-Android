package ir.amirhesambandegan.easify_location

import android.location.Location
import androidx.compose.runtime.*

/**
 * Reactively checks whether the user is inside a circular geofence boundary defined by coordinates and radius.
 *
 * Whenever [currentLocation], [centerLat], [centerLng], or [radiusMeters] changes, this composable recalculates
 * the distance using [LocationMath.calculateDistance] and updates the returned [State].
 *
 * @param currentLocation The live [Location] or `null` if location is not yet available.
 * @param centerLat Geofence center latitude in degrees.
 * @param centerLng Geofence center longitude in degrees.
 * @param radiusMeters Geofence radius in meters.
 * @return A [State] holding `true` if the [currentLocation] is within [radiusMeters] of the center coordinates, `false` otherwise.
 */
@Composable
fun rememberGeofenceState(
    currentLocation: Location?,
    centerLat: Double,
    centerLng: Double,
    radiusMeters: Float
): State<Boolean> {
    val isInside = remember { mutableStateOf(false) }

    LaunchedEffect(currentLocation, centerLat, centerLng, radiusMeters) {
        if (currentLocation == null) {
            isInside.value = false
            return@LaunchedEffect
        }
        val distance = LocationMath.calculateDistance(
            currentLocation.latitude,
            currentLocation.longitude,
            centerLat,
            centerLng
        )
        isInside.value = distance <= radiusMeters
    }

    return isInside
}
