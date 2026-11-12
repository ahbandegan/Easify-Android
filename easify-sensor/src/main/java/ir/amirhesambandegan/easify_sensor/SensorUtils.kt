package ir.amirhesambandegan.easify_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

/**
 * Utility helpers for querying available hardware sensors safely.
 */
object SensorUtils {

    /**
     * Returns `true` if [sensorType] is present on this device.
     */
    fun hasSensor(context: Context, sensorType: Int): Boolean {
        val sensorManager = context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            ?: return false
        return sensorManager.getDefaultSensor(sensorType) != null
    }

    /**
     * Returns all hardware sensors exposed by the device, or an empty list if [SensorManager] is unavailable.
     */
    fun getAllAvailableSensors(context: Context): List<Sensor> {
        val sensorManager = context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            ?: return emptyList()
        return sensorManager.getSensorList(Sensor.TYPE_ALL)
    }
}
