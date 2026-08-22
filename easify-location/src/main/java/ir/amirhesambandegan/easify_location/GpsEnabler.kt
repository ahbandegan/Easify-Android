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

class GpsEnabler(
    private val context: Context,
    private val launchResolution: (IntentSenderRequest) -> Unit
) {
    /**
     * Checks if GPS is enabled. If not, it shows the Google Play Services dialog to turn it on.
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
