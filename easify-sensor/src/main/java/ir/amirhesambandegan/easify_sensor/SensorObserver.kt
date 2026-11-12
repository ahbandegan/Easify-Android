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
 * Observes a hardware sensor and emits readings as a cold asynchronous [Flow] of [EasifySensorEvent].
 * Stores only `applicationContext` to prevent Activity context leaks.
 */
class SensorObserver(
    context: Context,
    private val sensorType: Int,
    private val samplingPeriod: Int = SensorManager.SENSOR_DELAY_UI
) {
    private val appContext = context.applicationContext
    private val sensorManager = appContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val sensor = sensorManager?.getDefaultSensor(sensorType)

    /**
     * Returns `true` when the requested hardware sensor exists on this device.
     */
    val isAvailable: Boolean
        get() = sensor != null

    /**
     * Cold [Flow] emitting [EasifySensorEvent] snapshots whenever sensor values update.
     * Completes immediately when [SensorManager] or the requested sensor is unavailable.
     */
    val sensorData: Flow<EasifySensorEvent> = callbackFlow {
        val manager = sensorManager
        val targetSensor = sensor
        if (manager == null || targetSensor == null) {
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

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        manager.registerListener(listener, targetSensor, samplingPeriod)

        awaitClose {
            manager.unregisterListener(listener)
        }
    }
}
