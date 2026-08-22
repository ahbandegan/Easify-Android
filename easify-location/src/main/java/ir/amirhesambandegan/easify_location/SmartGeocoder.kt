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
 * Reversely geocodes a [Location] into a human-readable [Address] asynchronously.
 *
 * Automatically handles API level compatibility: uses callback-based API on Android 13+ (API 33, [Build.VERSION_CODES.TIRAMISU]),
 * and synchronous fallback on older Android versions executed on [Dispatchers.IO].
 *
 * @receiver The [Location] containing latitude and longitude to reverse geocode.
 * @param context The Android [Context] used to instantiate [Geocoder].
 * @param locale The desired [Locale] for address formatting. Defaults to [Locale.getDefault].
 * @return The first matching [Address] if resolved, or `null` if resolution fails or no address is found.
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
 * Formats all address lines of an [Address] object into a single comma-separated string.
 *
 * @receiver The [Address] instance containing address lines.
 * @return A comma-separated [String] containing all available address lines.
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
 * Forward geocodes a human-readable address or place name into geographic coordinates wrapped in a [Location].
 *
 * Automatically handles API level compatibility: uses callback-based API on Android 13+ (API 33, [Build.VERSION_CODES.TIRAMISU]),
 * and synchronous fallback on older Android versions executed on [Dispatchers.IO].
 *
 * @receiver The [Geocoder] instance used to perform the lookup.
 * @param addressName The place name or street address string to search for.
 * @return A [Location] with provider set to `"geocoder"` containing the resolved latitude and longitude, or `null` if not found.
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
