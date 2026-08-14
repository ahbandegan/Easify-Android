package ir.amirhesambandegan.easify_permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Checks if a specific permission has been granted to the application.
 *
 * @param permission The permission to check.
 * @param context The application context.
 * @return A [PermissionStats] value representing the current status of the permission.
 */
fun isGranted(permission: String, context: Context) =
    PermissionStats.fromNumber(ContextCompat.checkSelfPermission(context, permission))
