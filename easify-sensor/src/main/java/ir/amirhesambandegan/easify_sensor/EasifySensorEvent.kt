package ir.amirhesambandegan.easify_sensor

import android.hardware.SensorEvent

/**
 * A simplified wrapper for [SensorEvent].
 */
data class EasifySensorEvent(
    val values: FloatArray,
    val accuracy: Int,
    val timestamp: Long
) {
    /**
     * Converts raw values to [ThreeAxisData]. Useful for Accelerometer, Gyroscope, etc.
     */
    fun toThreeAxisData(): ThreeAxisData {
        return ThreeAxisData(
            x = values.getOrElse(0) { 0f },
            y = values.getOrElse(1) { 0f },
            z = values.getOrElse(2) { 0f }
        )
    }

    /**
     * Converts raw values to [ProximityData].
     */
    fun toProximityData(): ProximityData {
        val distance = values.getOrElse(0) { 0f }
        // Standard Android behavior: 0.0 usually means "Near"
        return ProximityData(distance = distance, isNear = distance < 5f)
    }

    /**
     * Converts raw values to [SingleValueData]. Useful for Light, Pressure, etc.
     */
    fun toSingleValueData(): SingleValueData {
        return SingleValueData(value = values.getOrElse(0) { 0f })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EasifySensorEvent
        return values.contentEquals(other.values) && accuracy == other.accuracy && timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        var result = values.contentHashCode()
        result = 31 * result + accuracy
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
