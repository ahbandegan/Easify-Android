package ir.amirhesambandegan.easify_location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import ir.amirhesambandegan.easify_permission.RequestMultiplePermissions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * A reactive wrapper around Google Play Services' [FusedLocationProviderClient] for requesting location updates.
 */
class LocationTracker(context: Context) {
    private val appContext = context.applicationContext
    private val client: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(interval: Long = 10000L): Flow<Location?> = callbackFlow {
        val safeInterval = interval.coerceAtLeast(100L)
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, safeInterval).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                trySend(result.lastLocation)
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
            .addOnFailureListener { close(it) }

        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }
}

/**
 * Reactive state-holder associating a [LocationTracker] with permission request triggering.
 */
class LocationLauncherState(
    val tracker: LocationTracker,
    initialPermissionRequested: Boolean = false
) {
    var permissionRequested by mutableStateOf(initialPermissionRequested)
}

/**
 * Creates and remembers a [LocationTracker] and triggers runtime location permission prompts
 * reactively whenever `state.permissionRequested` is set to `true`.
 */
@Composable
fun rememberLocationTracker(): LocationLauncherState {
    val appContext = LocalContext.current.applicationContext
    val tracker = remember(appContext) { LocationTracker(appContext) }
    val state = remember(tracker) { LocationLauncherState(tracker, false) }

    if (state.permissionRequested) {
        RequestMultiplePermissions(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            onGrant = { state.permissionRequested = false },
            onDenied = { state.permissionRequested = false }
        )
    }

    return state
}
