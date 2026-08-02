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
 * Observes a specific hardware sensor and provides a reactive [Flow] of events.
 *
 * @param context The application context.
 * @param sensorType The type of sensor to observe (e.g., [Sensor.TYPE_ACCELEROMETER]).
 * @param samplingPeriod The rate at which sensor events are delivered (e.g., [SensorManager.SENSOR_DELAY_UI]).
 */
class SensorObserver(
    private val context: Context,
    private val sensorType: Int,
    private val samplingPeriod: Int = SensorManager.SENSOR_DELAY_UI
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(sensorType)

    /**
     * A [Flow] of [EasifySensorEvent]s from the hardware sensor.
     * Automatically registers/unregisters the listener based on flow collection.
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
