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
 * Android [Bitmap]-backed controller used by [Capturable].
 */
class CaptureController {
    internal var picture: Picture? = null
    internal var width: Int = 0
    internal var height: Int = 0

    fun capture(): Bitmap? {
        val pic = picture ?: return null
        if (width <= 0 || height <= 0) return null

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        canvas.drawPicture(pic)
        return bitmap
    }
}

@Composable
fun rememberCaptureController(): CaptureController = remember { CaptureController() }

/**
 * Wraps composable content on Android and records drawing commands for [CaptureController].
 */
@Composable
fun Capturable(
    controller: CaptureController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val picture = remember { Picture() }

    Box(
        modifier = modifier.drawWithCache {
            val width = size.width.toInt()
            val height = size.height.toInt()

            controller.picture = picture
            controller.width = width
            controller.height = height

            onDrawWithContent {
                if (width <= 0 || height <= 0) {
                    drawContent()
                    return@onDrawWithContent
                }

                val pictureCanvas = Canvas(picture.beginRecording(width, height))
                try {
                    draw(this, layoutDirection, pictureCanvas, size) {
                        this@onDrawWithContent.drawContent()
                    }
                } finally {
                    picture.endRecording()
                }

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawPicture(picture)
                }
            }
        }
    ) {
        content()
    }
}
