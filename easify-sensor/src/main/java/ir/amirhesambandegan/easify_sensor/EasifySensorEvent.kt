package ir.amirhesambandegan.easify_sensor

import android.hardware.SensorEvent

/**
 * A simplified data class wrapper for Android's [SensorEvent].
 * It holds the essential data required for processing sensor events, making it easier to mock and test.
 *
 * @property values An array of floats representing the sensor's raw values. The interpretation of these values depends on the sensor type.
 * @property accuracy The accuracy of the sensor event (e.g., [android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_HIGH]).
 * @property timestamp The time in nanoseconds at which the event happened.
 */
data class EasifySensorEvent(
    val values: FloatArray,
    val accuracy: Int,
    val timestamp: Long
) {
    /**
     * Converts the raw sensor values into a [ThreeAxisData] object.
     * This is particularly useful for sensors that provide data along three axes, such as Accelerometers, Gyroscopes, and Magnetic Field sensors.
     *
     * @return A [ThreeAxisData] instance containing the x, y, and z values. If any value is missing, it defaults to 0f.
     */
    fun toThreeAxisData(): ThreeAxisData {
        return ThreeAxisData(
            x = values.getOrElse(0) { 0f },
            y = values.getOrElse(1) { 0f },
            z = values.getOrElse(2) { 0f }
        )
    }

    /**
     * Converts the raw sensor values into a [ProximityData] object.
     * This is specifically tailored for Proximity sensors, interpreting the distance and whether an object is considered "near".
     *
     * @return A [ProximityData] instance containing the measured distance and a boolean indicating proximity.
     */
    fun toProximityData(): ProximityData {
        val distance = values.getOrElse(0) { 0f }
        // Standard Android behavior: 0.0 usually means "Near"
        return ProximityData(distance = distance, isNear = distance < 5f)
    }

    /**
     * Converts the raw sensor values into a [SingleValueData] object.
     * This is useful for environmental sensors that return a single measurement, such as Light (Lux), Pressure (hPa), or Ambient Temperature (°C).
     *
     * @return A [SingleValueData] instance containing the primary measured value. If the value is missing, it defaults to 0f.
     */
    fun toSingleValueData(): SingleValueData {
        return SingleValueData(value = values.getOrElse(0) { 0f })
    }

    /**
     * Compares this [EasifySensorEvent] to another object for equality.
     * Two [EasifySensorEvent] instances are considered equal if they have the same values array content, accuracy, and timestamp.
     *
     * @param other The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EasifySensorEvent
        return values.contentEquals(other.values) && accuracy == other.accuracy && timestamp == other.timestamp
    }

    /**
     * Computes the hash code for this [EasifySensorEvent].
     * The hash code is generated based on the values array, accuracy, and timestamp.
     *
     * @return The hash code value for this object.
     */
    override fun hashCode(): Int {
        var result = values.contentHashCode()
        result = 31 * result + accuracy
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
