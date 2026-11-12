package ir.amirhesambandegan.easify_file

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size

/**
 * Extracts a video frame thumbnail safely, always releasing [MediaMetadataRetriever] native resources.
 */
fun Uri.getVideoThumbnail(context: Context): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver.loadThumbnail(this, Size(512, 512), null)
        } else {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, this)
                retriever.getFrameAtTime(0L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } finally {
                runCatching { retriever.release() }
            }
        }
    } catch (_: Exception) {
        null
    }
}
