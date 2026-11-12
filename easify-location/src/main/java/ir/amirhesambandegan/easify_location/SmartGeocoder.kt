package ir.amirhesambandegan.easify_location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

/**
 * Reversely geocodes a [Location] into a human-readable [Address] asynchronously.
 */
suspend fun Location.toAddress(
    context: Context,
    locale: Locale = Locale.getDefault()
): Address? = withContext(Dispatchers.IO) {
    if (!Geocoder.isPresent()) return@withContext null
    val geocoder = Geocoder(context.applicationContext, locale)
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    if (continuation.isActive) {
                        continuation.resume(addresses.firstOrNull())
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        null
    }
}

fun Address.toFormattedString(): String {
    val builder = StringBuilder()
    for (i in 0..maxAddressLineIndex) {
        builder.append(getAddressLine(i))
        if (i < maxAddressLineIndex) builder.append(", ")
    }
    return builder.toString()
}

/**
 * Forward geocodes an address or place name into geographic coordinates wrapped in a [Location].
 */
suspend fun Geocoder.getCoordinates(addressName: String): Location? = withContext(Dispatchers.IO) {
    if (!Geocoder.isPresent() || addressName.isBlank()) return@withContext null
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                getFromLocationName(addressName, 1) { addresses ->
                    if (!continuation.isActive) return@getFromLocationName
                    val addr = addresses.firstOrNull()
                    if (addr != null) {
                        val loc = Location("geocoder").apply {
                            latitude = addr.latitude
                            longitude = addr.longitude
                        }
                        continuation.resume(loc)
                    } else {
                        continuation.resume(null)
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            val addr = getFromLocationName(addressName, 1)?.firstOrNull()
            addr?.let {
                Location("geocoder").apply {
                    latitude = it.latitude
                    longitude = it.longitude
                }
            }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        null
    }
}
