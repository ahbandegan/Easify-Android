package ir.amirhesambandegan.easify_location

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

/**
 * Filters out location updates that imply unrealistic speeds or sudden location teleportation/jumping.
 *
 * This operator computes the speed between consecutive valid location points based on the Haversine
 * distance and elapsed time. If the implied speed exceeds [maxSpeedKmh], the update is discarded as an anomaly.
 *
 * @receiver A [Flow] emitting nullable [Location] updates.
 * @param maxSpeedKmh The maximum plausible speed in kilometers per hour (e.g., 200 for automobiles, 1000 for airplanes). Defaults to 200 km/h.
 * @return A filtered [Flow] emitting only [Location] updates that do not exceed the speed threshold.
 */
fun Flow<Location?>.filterJumps(maxSpeedKmh: Float = 200f): Flow<Location?> {
    var lastValidLocation: Location? = null
    var lastValidTime: Long = 0
    
    return this.filter { newLocation ->
        if (newLocation == null) return@filter true
        
        if (lastValidLocation == null) {
            lastValidLocation = newLocation
            lastValidTime = System.currentTimeMillis()
            return@filter true
        }
        
        val distance = LocationMath.calculateDistance(
            lastValidLocation!!.latitude,
            lastValidLocation!!.longitude,
            newLocation.latitude,
            newLocation.longitude
        )
        
        val currentTime = System.currentTimeMillis()
        val timeDiffHours = (currentTime - lastValidTime) / 3600000.0
        
        if (timeDiffHours <= 0) return@filter false // Prevent division by zero
        
        val speedKmh = (distance / 1000.0) / timeDiffHours
        
        if (speedKmh > maxSpeedKmh) {
            false // It's a jump!
        } else {
            lastValidLocation = newLocation
            lastValidTime = currentTime
            true
        }
    }
}
