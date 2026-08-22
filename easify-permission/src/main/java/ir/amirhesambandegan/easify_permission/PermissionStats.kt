package ir.amirhesambandegan.easify_permission

import android.content.pm.PackageManager

/**
 * Represents the status of an Android permission.
 */
enum class PermissionStats {
    /** The permission has been granted. */
    PERMISSION_GRANTED,
    
    /** The permission has been denied. */
    PERMISSION_DENIED;

    companion object {
        /**
         * Converts a raw integer status from [PackageManager] to a [PermissionStats] instance.
         *
         * @param stats The integer status value (e.g., [PackageManager.PERMISSION_GRANTED] or [PackageManager.PERMISSION_DENIED]).
         * @return The corresponding [PermissionStats] or null if the status is unknown.
         */
        fun fromNumber(stats: Int): PermissionStats? =
            when (stats) {
                PackageManager.PERMISSION_GRANTED -> PERMISSION_GRANTED
                PackageManager.PERMISSION_DENIED -> PERMISSION_DENIED
                else -> null
            }
    }
}