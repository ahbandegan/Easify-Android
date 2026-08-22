package ir.amirhesambandegan.easify_location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Reverses a Location into a human-readable Address.
 */
suspend fun Location.toAddress(context: Context, locale: Locale = Locale.getDefault()): Address? = withContext(Dispatchers.IO) {
    val geocoder = Geocoder(context, locale)
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCoroutine { continuation ->
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    continuation.resume(addresses.firstOrNull())
                }
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Formats the Address object into a clean, readable string.
 */
fun Address.toFormattedString(): String {
    val builder = StringBuilder()
    for (i in 0..maxAddressLineIndex) {
        builder.append(getAddressLine(i))
        if (i < maxAddressLineIndex) builder.append(", ")
    }
    return builder.toString()
}

/**
 * Forwards a human-readable address into coordinates (Location).
 */
suspend fun Geocoder.getCoordinates(addressName: String): Location? = withContext(Dispatchers.IO) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCoroutine { continuation ->
                getFromLocationName(addressName, 1) { addresses ->
                    val addr = addresses.firstOrNull()
                    if (addr != null) {
                        val loc = Location("geocoder")
                        loc.latitude = addr.latitude
                        loc.longitude = addr.longitude
                        continuation.resume(loc)
                    } else {
                        continuation.resume(null)
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = getFromLocationName(addressName, 1)
            val addr = addresses?.firstOrNull()
            if (addr != null) {
                val loc = Location("geocoder")
                loc.latitude = addr.latitude
                loc.longitude = addr.longitude
                return@withContext loc
            }
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
