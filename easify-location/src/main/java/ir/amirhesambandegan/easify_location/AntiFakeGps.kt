package ir.amirhesambandegan.easify_location

import android.location.Location
import android.os.Build

/**
 * Checks if the current location is mocked/faked by a mock location app.
 */
fun Location.isMocked(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.isMock
    } else {
        @Suppress("DEPRECATION")
        this.isFromMockProvider
    }
}
