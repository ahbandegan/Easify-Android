package ir.amirhesambandegan.easify_sensor

import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Remembers a [SensorObserver] bound to `applicationContext` for the given [sensorType] and [samplingPeriod].
 */
@Composable
fun rememberSensorObserver(
    sensorType: Int,
    samplingPeriod: Int = SensorManager.SENSOR_DELAY_UI
): SensorObserver {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext, sensorType, samplingPeriod) {
        SensorObserver(appContext, sensorType, samplingPeriod)
    }
}
