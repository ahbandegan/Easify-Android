package ir.amirhesambandegan.easify_sensor

import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Creates and remembers a [SensorObserver] instance for a specific hardware sensor type.
 * This composable function ensures that the [SensorObserver] is properly initialized and retained across recompositions.
 *
 * @param sensorType The type of sensor to observe (e.g., [android.hardware.Sensor.TYPE_ACCELEROMETER], [android.hardware.Sensor.TYPE_LIGHT]).
 * @param samplingPeriod The requested rate at which sensor events should be delivered. Defaults to [SensorManager.SENSOR_DELAY_UI].
 *                       Other common values include [SensorManager.SENSOR_DELAY_NORMAL], [SensorManager.SENSOR_DELAY_GAME], or [SensorManager.SENSOR_DELAY_FASTEST].
 * @return A [SensorObserver] instance configured for the specified sensor and sampling period, managed within the composition.
 */
@Composable
fun rememberSensorObserver(
    sensorType: Int,
    samplingPeriod: Int = SensorManager.SENSOR_DELAY_UI
): SensorObserver {
    val context = LocalContext.current
    return remember(sensorType, samplingPeriod) {
        SensorObserver(context, sensorType, samplingPeriod)
    }
}
