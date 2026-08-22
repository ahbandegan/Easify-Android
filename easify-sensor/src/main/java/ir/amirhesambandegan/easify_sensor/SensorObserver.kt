package ir.amirhesambandegan.easify_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * A utility class that observes a specific hardware sensor and provides a reactive asynchronous [Flow] of its events.
 * This class abstracts away the traditional [SensorEventListener] implementation, making it easier to integrate sensor data into coroutine-based architectures.
 *
 * @property context The application context used to access the [SensorManager].
 * @property sensorType The integer type of the sensor to observe (e.g., [Sensor.TYPE_ACCELEROMETER]).
 * @property samplingPeriod The requested sampling rate for sensor events (e.g., [SensorManager.SENSOR_DELAY_UI]).
 */
class SensorObserver(
    private val context: Context,
    private val sensorType: Int,
    private val samplingPeriod: Int = SensorManager.SENSOR_DELAY_UI
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(sensorType)

    /**
     * A cold [Flow] that emits [EasifySensorEvent]s as they are received from the hardware sensor.
     * The underlying sensor listener is automatically registered when the flow is collected and unregistered when the collection is cancelled or closed.
     * If the requested sensor is not available on the device, the flow completes immediately without emitting any values.
     */
    val sensorData: Flow<EasifySensorEvent> = callbackFlow {
        if (sensor == null) {
            close()
            return@callbackFlow
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    trySend(
                        EasifySensorEvent(
                            values = it.values.copyOf(),
                            accuracy = it.accuracy,
                            timestamp = it.timestamp
                        )
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, sensor, samplingPeriod)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
