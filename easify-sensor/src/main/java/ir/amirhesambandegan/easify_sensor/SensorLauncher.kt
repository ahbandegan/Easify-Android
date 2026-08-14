package ir.amirhesambandegan.easify_sensor

import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Creates and remembers a [SensorObserver] for a specific sensor type.
 *
 * @param sensorType The type of sensor (e.g., [android.hardware.Sensor.TYPE_ACCELEROMETER]).
 * @param samplingPeriod The rate of events. Defaults to [SensorManager.SENSOR_DELAY_UI].
 * @return A [SensorObserver] instance managed within the composition.
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
