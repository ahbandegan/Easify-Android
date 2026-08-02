package ir.amirhesambandegan.easify_permission

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import ir.amirhesambandegan.easify_file.MediaType

/**
 * Utility functions for handling Android permissions.
 */
object PermissionUtils {
    /**
     * Determines the required permissions for picking media files based on the [mediaType]
     * and the device's API level.
     *
     * @param context The application context.
     * @param mediaType The type of media to be picked.
     * @return A list of required permission strings. Returns an empty list if no permissions are needed.
     */
    fun getRequiredPermissions(context: Context, mediaType: MediaType): List<String> {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context)) {
            return emptyList()
        }

        return if (Build.VERSION.SDK_INT >= 33) {
            when (mediaType) {
                MediaType.ImageOnly -> listOf(Manifest.permission.READ_MEDIA_IMAGES)
                MediaType.VideoOnly -> listOf(Manifest.permission.READ_MEDIA_VIDEO)
                MediaType.ImageAndVideo -> listOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            }
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}
