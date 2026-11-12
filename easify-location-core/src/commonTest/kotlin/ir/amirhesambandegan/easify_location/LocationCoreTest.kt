package ir.amirhesambandegan.easify_location

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocationCoreTest {

    @Test
    fun distanceBearingAndGeofenceWork() {
        // Tehran Azadi Tower -> Milad Tower (~4.8 km)
        val d = LocationMath.calculateDistance(35.6997, 51.3380, 35.7448, 51.3753)
        assertTrue(d in 5500f..6500f)
        assertTrue(LocationMath.isWithinRadius(35.6997, 51.3380, 35.7448, 51.3753, 7000f))
        assertFalse(LocationMath.isWithinRadius(35.6997, 51.3380, 35.7448, 51.3753, 1000f))

        val bearing = LocationMath.calculateBearing(35.0, 51.0, 36.0, 51.0)
        assertTrue(bearing < 1f || bearing > 359f)
    }

    @Test
    fun antiJumpFiltersTeleportationWithIsolatedCollectorState() = runTest {
        val stream = flowOf(
            GeoPoint(35.7000, 51.3400, timestampMillis = 1000L),
            // 100 km jump in 1 second -> should be rejected
            GeoPoint(36.7000, 51.3400, timestampMillis = 2000L),
            // Realistic ~14m movement in 2 seconds -> should pass
            GeoPoint(35.7001, 51.3401, timestampMillis = 3000L)
        ).filterPointJumps(maxSpeedKmh = 200f)

        val firstPass = stream.toList().filterNotNull()
        val secondPass = stream.toList().filterNotNull()

        assertEquals(2, firstPass.size)
        assertEquals(2, secondPass.size)
    }
}
