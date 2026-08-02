package ir.amirhesambandegan.easify_sensor

/**
 * Data class for sensors providing 3-axis data (e.g., Accelerometer, Gyroscope).
 */
data class ThreeAxisData(val x: Float, val y: Float, val z: Float)

/**
 * Data class for the Proximity sensor.
 * @property distance The measured distance in centimeters.
 * @property isNear True if an object is close to the sensor.
 */
data class ProximityData(val distance: Float, val isNear: Boolean)

/**
 * Data class for environmental sensors (e.g., Light, Pressure, Temperature).
 * @property value The primary value measured by the sensor (e.g., Lux for light).
 */
data class SingleValueData(val value: Float)
