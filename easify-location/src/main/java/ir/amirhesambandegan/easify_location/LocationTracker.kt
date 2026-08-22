package ir.amirhesambandegan.easify_location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.*
import ir.amirhesambandegan.easify_permission.RequestMultiplePermissions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * A reactive wrapper around Google Play Services' [FusedLocationProviderClient] for requesting location updates.
 *
 * @param context The Android [Context] used to initialize [FusedLocationProviderClient].
 */
class LocationTracker(context: Context) {
    /**
     * Internal Google Play Services fused location provider client.
     */
    private val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Returns a [Flow] that continuously emits the device's [Location] updates at the specified interval.
     *
     * Note: Requires `ACCESS_FINE_LOCATION` or `ACCESS_COARSE_LOCATION` runtime permissions before collection.
     *
     * @param interval The desired interval for location updates in milliseconds. Defaults to 10,000 ms (10 seconds).
     * @return A cold [Flow] that requests location updates on collection and unregisters on cancellation.
     */
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(interval: Long = 10000L): Flow<Location?> = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).build()
        
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                trySend(result.lastLocation)
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        
        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }
}

/**
 * State-holding class that associates a [LocationTracker] with permission request state.
 *
 * @property tracker The [LocationTracker] instance used for streaming location updates.
 * @property permissionRequested Whether location permissions are currently being requested.
 */
class LocationLauncherState(
    val tracker: LocationTracker,
    var permissionRequested: Boolean
)

/**
 * Creates and remembers a [LocationTracker] and manages the lifecycle of requesting location permissions.
 *
 * Automatically triggers permission requests for [Manifest.permission.ACCESS_FINE_LOCATION] and
 * [Manifest.permission.ACCESS_COARSE_LOCATION] when requested.
 *
 * @return A [LocationLauncherState] instance containing the tracker and permission status.
 */
@Composable
fun rememberLocationTracker(): LocationLauncherState {
    val context = LocalContext.current
    val tracker = remember { LocationTracker(context) }
    var permissionRequested by remember { mutableStateOf(false) }
    
    val state = remember { LocationLauncherState(tracker, permissionRequested) }
    state.permissionRequested = permissionRequested

    if (permissionRequested) {
        RequestMultiplePermissions(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            onGrant = {
                permissionRequested = false
            },
            onDenied = {
                permissionRequested = false
            }
        )
    }

    return state
}
