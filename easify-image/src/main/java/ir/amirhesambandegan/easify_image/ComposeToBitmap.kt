package ir.amirhesambandegan.easify_image

import android.graphics.Bitmap
import android.graphics.Picture
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

/**
 * A controller used for capturing composable content into a [Bitmap].
 */
class CaptureController {
    /**
     * The [Picture] used to record drawing operations.
     */
    internal var picture: Picture? = null

    /**
     * The width of the captured area in pixels.
     */
    internal var width: Int = 0

    /**
     * The height of the captured area in pixels.
     */
    internal var height: Int = 0
    
    /**
     * Captures the recorded picture into a new [Bitmap].
     *
     * @return The captured [Bitmap], or null if the picture is not available or dimensions are invalid.
     */
    fun capture(): Bitmap? {
        val pic = picture ?: return null
        if (width <= 0 || height <= 0) return null
        
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        // Draw a white background by default, otherwise transparent parts become black
        canvas.drawColor(android.graphics.Color.WHITE)
        canvas.drawPicture(pic)
        return bitmap
    }
}

/**
 * Creates and remembers a [CaptureController] across recompositions.
 *
 * @return A new instance of [CaptureController].
 */
@Composable
fun rememberCaptureController(): CaptureController {
    return remember { CaptureController() }
}

/**
 * Wraps a Compose UI and allows capturing it to a Bitmap.
 * This implementation is 100% native to Compose and avoids AndroidView interop bugs.
 */
@Composable
fun Capturable(
    controller: CaptureController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val picture = remember { Picture() }
    
    Box(
        modifier = modifier
            .drawWithCache {
                val width = this.size.width.toInt()
                val height = this.size.height.toInt()
                
                controller.picture = picture
                controller.width = width
                controller.height = height
                
                onDrawWithContent {
                    val pictureCanvas = Canvas(picture.beginRecording(width, height))
                    draw(this, this.layoutDirection, pictureCanvas, this.size) {
                        this@onDrawWithContent.drawContent()
                    }
                    picture.endRecording()
                    
                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawPicture(picture)
                    }
                }
            }
    ) {
        content()
    }
}
