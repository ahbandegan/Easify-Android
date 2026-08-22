package ir.amirhesambandegan.easify_location

import android.location.Location
import androidx.compose.runtime.*

/**
 * Reactively checks if the user is inside a circular geofence.
 * @param currentLocation The live location flow or state.
 * @param centerLat Geofence center latitude.
 * @param centerLng Geofence center longitude.
 * @param radiusMeters Geofence radius in meters.
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
