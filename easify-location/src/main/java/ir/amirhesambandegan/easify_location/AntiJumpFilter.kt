package ir.amirhesambandegan.easify_location

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

/**
 * Filters out location updates that imply impossible speeds (GPS jumping/teleporting).
 * @param maxSpeedKmh Maximum reasonable speed (e.g., 200 for cars, 1000 for airplanes).
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
