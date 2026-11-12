package ir.amirhesambandegan.easify_location

import android.location.Location

/**
 * Converts an Android [Location] into a multiplatform [GeoPoint].
 */
fun Location.toGeoPoint(): GeoPoint = GeoPoint(
    latitude = latitude,
    longitude = longitude,
    accuracyMeters = if (hasAccuracy()) accuracy else null,
    speedMps = if (hasSpeed()) speed else null,
    timestampMillis = time
)
