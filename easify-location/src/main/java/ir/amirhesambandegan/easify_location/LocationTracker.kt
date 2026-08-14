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
 * A reactive wrapper for FusedLocationProviderClient.
 */
class LocationTracker(context: Context) {
    private val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     * A [Flow] that emits the device's location updates.
     * @param interval The desired interval for location updates in milliseconds.
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
 * A state-holding object for the Location Launcher.
 */
class LocationLauncherState(
    val tracker: LocationTracker,
    var permissionRequested: Boolean
)

/**
 * Creates and remembers a [LocationTracker] that handles permissions automatically.
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
