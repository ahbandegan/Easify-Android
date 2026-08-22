package ir.amirhesambandegan.easify_location

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task

/**
 * Utility to verify if device location services (GPS) are enabled and request the user to turn them on via Google Play Services dialog.
 *
 * @property context The Android [Context] used to obtain Google Play [LocationServices] settings client.
 * @property launchResolution Callback invoked when a resolution intent must be launched (e.g. via [IntentSenderRequest]).
 */
class GpsEnabler(
    private val context: Context,
    private val launchResolution: (IntentSenderRequest) -> Unit
) {
    /**
     * Checks if location settings are satisfied for high accuracy. If not, prompts the user with the system resolution dialog.
     *
     * @param onResult Callback invoked with `true` if GPS is already enabled, or `false` if settings resolution failed or was rejected.
     */
    fun requestGps(onResult: (Boolean) -> Unit = {}) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            onResult(true) // GPS is already on
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    launchResolution(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    onResult(false)
                }
            } else {
                onResult(false)
            }
        }
    }
}

/**
 * Creates and remembers a [GpsEnabler] instance within a Jetpack Compose environment.
 *
 * Uses [rememberLauncherForActivityResult] to handle the GPS resolution dialog result.
 *
 * @param onResult Callback invoked with `true` if the user enabled GPS, `false` otherwise.
 * @return A remembered [GpsEnabler] instance configured for the calling composable.
 */
@Composable
fun rememberGpsEnabler(onResult: (Boolean) -> Unit): GpsEnabler {
    val context = LocalContext.current
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        onResult(result.resultCode == Activity.RESULT_OK)
    }

    return remember(context, launcher) {
        GpsEnabler(context) { intentSenderRequest ->
            launcher.launch(intentSenderRequest)
        }
    }
}
