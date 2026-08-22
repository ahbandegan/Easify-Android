package ir.amirhesambandegan.easify_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

/**
 * A utility singleton object providing helpful methods for interacting with and querying hardware sensors on an Android device.
 */
object SensorUtils {

    /**
     * Checks if a specific type of hardware sensor is available on the current device.
     * This is useful for verifying hardware capabilities before attempting to register sensor listeners.
     *
     * @param context The application context used to access the [SensorManager].
     * @param sensorType The integer constant representing the type of sensor to check (e.g., [Sensor.TYPE_GYROSCOPE]).
     * @return True if the device has the specified sensor, false otherwise.
     */
    fun hasSensor(context: Context, sensorType: Int): Boolean {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getDefaultSensor(sensorType) != null
    }

    /**
     * Retrieves a comprehensive list of all hardware sensors available on the current device.
     * This can be used for diagnostics, logging, or allowing users to select which sensors to monitor.
     *
     * @param context The application context used to access the [SensorManager].
     * @return A [List] of [Sensor] objects representing every available sensor on the device.
     */
    fun getAllAvailableSensors(context: Context): List<Sensor> {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getSensorList(Sensor.TYPE_ALL)
    }
}
