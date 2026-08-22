package ir.amirhesambandegan.easify_file

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size

fun Uri.getVideoThumbnail(context: Context): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver.loadThumbnail(this, Size(512, 512), null)
        } else {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, this)
            val bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            retriever.release()
            bitmap
        }
    } catch (e: Exception) {
        null
    }
}
