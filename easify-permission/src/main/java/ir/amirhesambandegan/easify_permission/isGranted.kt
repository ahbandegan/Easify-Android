package ir.amirhesambandegan.easify_permission

import android.content.Context
import androidx.core.content.ContextCompat

/**
 * Checks if a specific runtime permission has been granted to the application.
 *
 * @param permission The manifest permission string to check (e.g. `Manifest.permission.CAMERA`).
 * @param context The application or activity [Context].
 * @return A [PermissionStats] value representing the current status of the permission ([PermissionStats.PERMISSION_GRANTED] or [PermissionStats.PERMISSION_DENIED]), or `null` if unknown.
 */
fun isGranted(permission: String, context: Context) =
    PermissionStats.fromNumber(ContextCompat.checkSelfPermission(context, permission))
