package ir.amirhesambandegan.easify_location

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Filters out location updates implying unrealistic speeds (> [maxSpeedKmh]),
 * keeping filter state completely isolated per Flow collector.
 */
fun Flow<Location?>.filterJumps(maxSpeedKmh: Float = 200f): Flow<Location?> = flow {
    var lastValidLocation: Location? = null
    var lastValidTimeMillis: Long = 0L

    collect { newLocation ->
        if (newLocation == null) {
            emit(null)
            return@collect
        }

        val currentTime = if (newLocation.time > 0L) newLocation.time else System.currentTimeMillis()
        val previous = lastValidLocation
        if (previous == null) {
            lastValidLocation = newLocation
            lastValidTimeMillis = currentTime
            emit(newLocation)
            return@collect
        }

        val diffMillis = currentTime - lastValidTimeMillis
        if (diffMillis <= 0L) return@collect

        val distance = LocationMath.calculateDistance(
            previous.latitude,
            previous.longitude,
            newLocation.latitude,
            newLocation.longitude
        )

        val timeDiffHours = diffMillis / 3_600_000.0
        val speedKmh = (distance / 1000.0) / timeDiffHours

        if (speedKmh <= maxSpeedKmh) {
            lastValidLocation = newLocation
            lastValidTimeMillis = currentTime
            emit(newLocation)
        }
    }
}
