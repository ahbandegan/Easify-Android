package ir.amirhesambandegan.easify_sensor

/**
 * Data class representing measurements from sensors that provide data across three dimensions (X, Y, and Z axes).
 * Commonly used for sensors like Accelerometers, Gyroscopes, and Magnetometers.
 *
 * @property x The measurement along the X-axis.
 * @property y The measurement along the Y-axis.
 * @property z The measurement along the Z-axis.
 */
data class ThreeAxisData(val x: Float, val y: Float, val z: Float)

/**
 * Data class representing a measurement from the Proximity sensor.
 * It provides both the raw distance and a boolean flag indicating if an object is considered close.
 *
 * @property distance The measured distance from the sensor to the object, typically in centimeters.
 * @property isNear A boolean flag indicating whether an object is close to the sensor. True if the object is near (usually distance < 5cm).
 */
data class ProximityData(val distance: Float, val isNear: Boolean)

/**
 * Data class representing a single-value measurement from environmental sensors.
 * Used for sensors that provide one primary data point, such as Light (illuminance in lx), Pressure (hPa), or Ambient Temperature (°C).
 *
 * @property value The primary measured value from the sensor.
 */
data class SingleValueData(val value: Float)
