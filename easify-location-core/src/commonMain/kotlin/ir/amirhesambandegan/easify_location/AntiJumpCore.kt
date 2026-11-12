package ir.amirhesambandegan.easify_location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Filters out unrealistic GPS teleportation jumps while keeping state completely isolated per Flow collector.
 */
@OptIn(ExperimentalTime::class)
fun Flow<GeoPoint?>.filterPointJumps(
    maxSpeedKmh: Float = 200f,
    nowProvider: () -> Long = { Clock.System.now().toEpochMilliseconds() }
): Flow<GeoPoint?> = flow {
    var lastValidPoint: GeoPoint? = null
    var lastValidTimeMillis: Long = 0L

    collect { newPoint ->
        if (newPoint == null) {
            emit(null)
            return@collect
        }

        val currentTime = if (newPoint.timestampMillis > 0L) newPoint.timestampMillis else nowProvider()
        val prev = lastValidPoint
        if (prev == null) {
            lastValidPoint = newPoint
            lastValidTimeMillis = currentTime
            emit(newPoint)
            return@collect
        }

        val diffMillis = currentTime - lastValidTimeMillis
        if (diffMillis <= 0L) {
            return@collect
        }

        val distanceMeters = LocationMath.calculateDistance(
            prev.latitude,
            prev.longitude,
            newPoint.latitude,
            newPoint.longitude
        )
        val timeDiffHours = diffMillis / 3_600_000.0
        val speedKmh = (distanceMeters / 1000.0) / timeDiffHours

        if (speedKmh <= maxSpeedKmh) {
            lastValidPoint = newPoint
            lastValidTimeMillis = currentTime
            emit(newPoint)
        }
    }
}
