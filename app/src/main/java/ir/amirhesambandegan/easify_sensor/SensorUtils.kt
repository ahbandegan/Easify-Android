package ir.amirhesambandegan.easify_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

/**
 * Utility functions for hardware sensors.
 */
object SensorUtils {

    /**
     * Checks if a specific sensor type is available on the device.
     */
    fun hasSensor(context: Context, sensorType: Int): Boolean {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getDefaultSensor(sensorType) != null
    }

    /**
     * Returns a list of all available sensors on the device.
     */
    fun getAllAvailableSensors(context: Context): List<Sensor> {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getSensorList(Sensor.TYPE_ALL)
    }
}
